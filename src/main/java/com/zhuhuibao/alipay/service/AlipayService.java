package com.zhuhuibao.alipay.service;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.zhuhuibao.alipay.config.AliPayConfig;
import com.zhuhuibao.alipay.util.AlipayNotify;
import com.zhuhuibao.alipay.util.AlipaySubmit;
import com.zhuhuibao.common.constant.*;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.order.entity.*;
import com.zhuhuibao.mybatis.order.service.*;
import com.zhuhuibao.utils.CommonUtils;
import com.zhuhuibao.utils.IdGenerator;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.utils.PropertiesUtils;
import com.zhuhuibao.utils.convert.DateConvert;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 支付宝服务入口
 */
@Service
@Transactional
public class AlipayService {
    private static final Logger log = LoggerFactory.getLogger(AlipayService.class);


    @Autowired
    private AliPayConfig aliPayConfig;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderGoodsService orderGoodsService;

    @Autowired
    private AlipayCallbackLogService alipayCallbackLogService;

    @Autowired
    private AlipayRefundCallbackLogService refundCallbackLogService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private PwdTickerService pwdTickerService;

    @Autowired
    private PublishCourseService publishCourseService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private OrderSmsService orderSmsService;


    /**
     * 支付包支付请求
     *
     * @param msgParam 请求参数集合
     * @param method   提交方式。两个值可选：post、get
     * @return html
     */
    public String alipay(Map<String, String> msgParam, String method) throws Exception {

        //生成订单号
        String orderNo = IdGenerator.createOrderNo();
        msgParam.put("orderNo", orderNo);

        //支付请求参数校验
        checkPayParams(msgParam);

        //记录订单信息和其他相关处理
        beforePayDeal(msgParam);

        //支付操作
        return doPay(msgParam, method);
    }

    /**
     * 支付宝退款请求
     *
     * @param msgParam 请求参数集合
     * @param method   提交方式。两个值可选：post、get
     * @return
     * @throws Exception
     */
    public String alirefund(Map<String, String> msgParam, String method) throws Exception {

        //生成批量退款批次号
        String bathcNo = IdGenerator.createBatchNo();
        msgParam.put("batchNo", bathcNo);

        //退款请求参数校验
        checkRefundParams(msgParam);

        //记录退款申请信息
        genRefundRecord(msgParam);

        return doRefund(msgParam, method);
    }

    /**
     * 记录退款申请记录 (t_o_refund)
     *
     * @param msgParam params
     */
    private void genRefundRecord(Map<String, String> msgParam) throws ParseException {
        log.debug("request params:{}", msgParam.toString());
        Refund refund = new Refund();
        refund.setBatchNo(msgParam.get("batchNo"));
        refund.setBatchNum(Integer.valueOf(msgParam.get("batchNum")));
        refund.setOperatorId(Integer.valueOf(msgParam.get("operatorId")));
        refund.setOrderNo(msgParam.get("orderNos"));
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        refund.setRefundDate(sf.parse(msgParam.get("refundDate")));
        refund.setTotalFee(Long.valueOf(msgParam.get("totalFee")));

        refundService.insert(refund);

    }

    /**
     * 退款请求参数校验
     *
     * @param msgParam 请求参数集合
     */
    private void checkRefundParams(Map<String, String> msgParam) {
        String orderNo = msgParam.get("orderNos");//退款订单编号  逗号隔开
        if (StringUtils.isEmpty(orderNo)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "退款订单编号不能为空");
        }

        String operatorId = msgParam.get("operatorId");//操作员ID
        if (StringUtils.isEmpty(operatorId)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "操作员ID不能为空");
        }

        String refundDate = msgParam.get("refundDate");//退款申请时间
        if (StringUtils.isEmpty(refundDate)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "退款申请时间不能为空");
        }

        String totalFee = msgParam.get("totalFee");//退款总金额
        if (StringUtils.isEmpty(totalFee)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "退款总金额不能为空");
        }

        String batchNum = msgParam.get("batchNum");//退款笔数
        if (StringUtils.isEmpty(batchNum)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "退款笔数不能为空");
        }

    }

    /**
     * 支付宝同步通知 {不使用}
     *
     * @param request   request
     * @param tradeType 交易流水类型
     * @return mv
     */
    public ModelAndView syncNotify(HttpServletRequest request, String tradeType) {

        ModelAndView modelAndView = new ModelAndView();
        RedirectView rv = new RedirectView(PropertiesUtils.getValue("host.ip") + "/" + PropertiesUtils.getValue("alipay_return_url"));
        modelAndView.setView(rv);
        try {
            // 获取返回信息
            // 获取支付宝GET过来反馈信息
            Map<String, String> params = getRequestParams(request);

            // 计算得出通知验证结果
            log.info("******支付宝同步回调校验参数信息开始*******");
            boolean verify_result = AlipayNotify.verify(params);
            log.info("******支付宝同步回调校验参数信息结果=" + verify_result);

            if (verify_result) { //验证成功

                if (params.get("trade_status").equals("TRADE_FINISHED")
                        || params.get("trade_status").equals("TRADE_SUCCESS")) {
                    Map<String, String> resultMap = tradeSuccessDeal(params,
                            PayConstants.NotifyType.SYNC.toString(), tradeType);
                    log.info("***同步回调：支付平台回调发起发支付方结果：" + resultMap);
                    if (resultMap != null
                            && String.valueOf(PayConstants.HTTP_SUCCESS_CODE)
                            .equals(resultMap.get("statusCode"))) {
                        if ("SUCCESS".equals(resultMap.get("result"))) {
                            modelAndView.addObject("result", "success");
                            modelAndView.addObject("msg", "支付成功");
                        }
                    }
                } else {
                    modelAndView.addObject("result", "fail");
                    modelAndView.addObject("msg", "支付成功");
                }
            } else { //验证失败
                modelAndView.addObject("result", "fail");
                modelAndView.addObject("msg", "验证失败");
            }


        } catch (Exception e) {
            log.error("获取out对象异常" + e.getMessage());
            e.printStackTrace();
        }
        return modelAndView;

    }

    /**
     * 支付宝异步通知
     *
     * @param request   request
     * @param response  response
     * @param tradeType 交易流水类型 1:支付 2:退款
     */
    public void asyncNotify(HttpServletRequest request, HttpServletResponse response, String tradeType) {
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            // 获取支付宝POST过来反馈信息
            Map<String, String> params = getRequestParams(request);

            log.info("******支付宝异步回调校验参数信息开始*******");
            boolean verify_result = AlipayNotify.verify(params);
            log.info("******支付宝异步回调校验参数信息结果=" + verify_result);

            if (verify_result) {// 验证成功

                if (params.get("trade_status").equals("TRADE_FINISHED")
                        || params.get("trade_status").equals("TRADE_SUCCESS")) {

                    //判断该笔订单是否在商户网站中已经做过处理
                    //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                    //如果有做过处理，不执行商户的业务程序
                    if (tradeType.equals(PayConstants.TradeType.PAY.toString())) {
                        Order order = orderService.findByOrderNo(params.get("out_trade_no"));
                        BigDecimal price = new BigDecimal(params.get("price"));
                        BigDecimal totalFee = price.multiply(new BigDecimal(params.get("quantity")));

                        if (!String.valueOf(order.getPayAmount()).equals(totalFee.toString())) {
                            log.error("支付宝返回交易金额[{}] 与 订单支付金额[{}] 不符合", totalFee.toString(), order.getPayAmount());
                            out.println("fail");
                        }
                        if (!order.getSellerId().equals(params.get("seller_id"))) {
                            log.error("支付宝返回卖家支付宝用户号[{}] 与 订单卖家支付宝用户号[{}] 不符合", params.get("seller_id"), order.getSellerId());
                            out.println("fail");
                        }
                    }

                    //业务逻辑处理
                    Map<String, String> resultMap =
                            tradeSuccessDeal(params, PayConstants.NotifyType.ASYNC.toString(), tradeType);
                    log.info("***异步回调：支付平台回调发起发支付方结果：" + resultMap);
                    if (resultMap != null
                            && String.valueOf(PayConstants.HTTP_SUCCESS_CODE).equals(resultMap.get("statusCode"))) {
                        if ("SUCCESS".equals(resultMap.get("result"))) {
                            out.println("success");
                        }
                    }
                }

            } else {// 验证失败
                log.error("");
                out.println("fail");
            }
        } catch (Exception e) {
            log.error("获取out对象异常" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 订单相关操作  (t_o_order t_o_order_goods t_o_pwdticket t_p_group_publishCourse)
     * 事务处理,异常回滚
     *
     * @param msgParam 请求参数
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void beforePayDeal(Map<String, String> msgParam) throws IOException {
        log.info("request params:{}", msgParam.toString());

        //订单记录
        genOrderRecord(msgParam);
        //订单商品详情
        genOrderGoodsRecord(msgParam);
        //发票信息
        genInvoiceRecord(msgParam);

        //如果是技术培训 专家培训 需要记录订单SN码 t_o_pwdticket
        if (msgParam.get("goodsType").equals(OrderConstants.GoodsType.JSPX.toString())
                || msgParam.get("goodsType").equals(OrderConstants.GoodsType.ZJPX.toString())) {

            //根据订单产品数量生成SN码
            genSNcode(msgParam, msgParam.get("goodsType"));

            //修改课程库存数量
            updateSubStock(msgParam);

        }
    }

    /**
     * 需要发票 生成发票记录
     *
     * @param msgParam
     */
    private void genInvoiceRecord(Map<String, String> msgParam) {
        String needInvoice = msgParam.get("needInvoice");
        if ("true".equals(needInvoice)) {
            Invoice invoice = new Invoice();
            invoice.setCreateTime(new Date());
            invoice.setInvoiceTitle(msgParam.get("invoiceTitle"));
            invoice.setInvoiceType(Integer.valueOf(msgParam.get("invoiceType")));
            invoice.setOrderNo(msgParam.get("orderNo"));
            invoiceService.insert(invoice);
        }

    }

    /**
     * 修改课程库存 {减库存}
     *
     * @param params
     */
    private void updateSubStock(Map<String, String> params) {
        Long courseId = Long.valueOf(params.get("goodsId"));
        int number = Integer.valueOf(params.get("number"));
        publishCourseService.updateSubStockNum(courseId, number);

    }

    /**
     * 根据订单生产SN码
     *
     * @param msgParam
     */
    private void genSNcode(Map<String, String> msgParam, String type) throws IOException {

        int num = Integer.valueOf(msgParam.get("number"));
        List<PwdTicket> list = new ArrayList<>();
        List<String> snCodeList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            PwdTicket pwdTicket = new PwdTicket();
            String snCode = IdGenerator.createSNcode();
            pwdTicket.setSnCode(snCode);
            pwdTicket.setMobile(msgParam.get("mobile"));
            pwdTicket.setOrderNo(msgParam.get("orderNo"));
            pwdTicket.setCourseId(Long.valueOf(msgParam.get("goodsId")));
            pwdTicket.setTicketType(type);
            list.add(pwdTicket);
            snCodeList.add(snCode);
        }
        pwdTickerService.batchInsert(list);

        //短信记录
        PublishCourse course = publishCourseService.getCourseById(Long.valueOf(msgParam.get("goodsId")));
        StringBuilder sb = new StringBuilder();
        for (String code : snCodeList) {
            sb.append(code).append(",");
        }
        String temp = sb.toString();
        String codes = temp.substring(0, temp.length() - 1);

        Map<String, String> smsMap = new LinkedHashMap<>();
        smsMap.put("name", course.getTitle());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        smsMap.put("time", format.format(course.getSaleTime()));
        smsMap.put("code", codes);
        Gson gson = new Gson();
        String content = gson.toJson(smsMap);
        OrderSms orderSms = new OrderSms();
        orderSms.setOrderNo(msgParam.get("orderNo"));
        orderSms.setMobile(msgParam.get("mobile"));
        orderSms.setContent(content);
        orderSms.setStatus(OrderConstants.SmsStatus.WAITING.toString());
        //开课短信
        orderSms.setTemplateCode(PropertiesUtils.getValue("course_begin_sms_template_code"));
        orderSmsService.insert(orderSms);

        //终止课程短信
        orderSms.setTemplateCode(PropertiesUtils.getValue("course_before_stop_sms_template_code"));
        orderSmsService.insert(orderSms);

        //终止课程短信
        orderSms.setTemplateCode(PropertiesUtils.getValue("course_before_autostop_sms_template_code"));
        orderSmsService.insert(orderSms);

        //终止课程短信
        orderSms.setTemplateCode(PropertiesUtils.getValue("course_after_stop_sms_template_code"));
        orderSmsService.insert(orderSms);


    }

    /**
     * 生成订单商品详情记录
     *
     * @param msgParam
     */
    private void genOrderGoodsRecord(Map<String, String> msgParam) {
        //订单商品
        OrderGoods orderGoods = new OrderGoods();
        orderGoods.setGoodsId(Long.valueOf(msgParam.get("goodsId")));
        orderGoods.setGoodsName(msgParam.get("goodsName"));

        orderGoods.setGoodsPrice(new BigDecimal(msgParam.get("goodsPrice")));
        orderGoods.setNumber(Integer.valueOf(msgParam.get("number")));
        orderGoods.setOrderNo(msgParam.get("orderNo"));
        orderGoods.setCreateTime(new Date());

        orderGoodsService.insert(orderGoods);
    }

    /**
     * 生成订单记录
     *
     * @param msgParam
     */
    private void genOrderRecord(Map<String, String> msgParam) {
        Order order = new Order();
        order.setOrderNo(msgParam.get("orderNo"));
        order.setBuyerId(Long.valueOf(msgParam.get("buyerId")));
        order.setSellerId(msgParam.get("partner"));
//        Date dealTime;
//        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        dealTime = sf.parse(msgParam.get("dealTime"));
//        order.setDealTime(dealTime);
        order.setDealTime(new Date());
        String payPrice = msgParam.get("goodsPrice");
        String number = msgParam.get("number");
        BigDecimal price = new BigDecimal(payPrice);
        BigDecimal num = new BigDecimal(number);
        BigDecimal amount = price.multiply(num);
        order.setAmount(amount); //订单总金额
//        String payAmount = msgParam.get("payAmount");
        order.setPayAmount(amount);  //交易金额
        order.setGoodsType(msgParam.get("goodsType"));
        order.setPayMode(msgParam.get("payMode"));
        order.setStatus(PayConstants.OrderStatus.WZF.toString());

        orderService.insert(order);
    }


    /**
     * 支付操作
     *
     * @param msgParam 请求参数
     * @param method   请求方法{get,post}
     * @return html
     */
    private String doPay(Map<String, String> msgParam, String method) {

        Map<String, String> sParaTemp = new HashMap<>();
        //基本参数
        sParaTemp.put("_input_charset", aliPayConfig.getInputCharset());//编码格式
        sParaTemp.put("payment_type", aliPayConfig.getPaymentType());   //支付类型 ，无需修改
        sParaTemp.put("service", msgParam.get("service"));              //接口名称
        sParaTemp.put("notify_url", msgParam.get("notifyUrl"));         //服务器异步通知页面路径
//        sParaTemp.put("return_url", msgParam.get("returnUrl"));       //页面跳转同步通知页面路径
        sParaTemp.put("partner", msgParam.get("partner"));              //合作伙伴ID  == 商家支付宝账户号
        sParaTemp.put("seller_id", msgParam.get("partner"));            //partner = seller_id

        //业务参数
        sParaTemp.put("goods_type", msgParam.get("alipay_goods_type"));   //商品类型(0:虚拟类商品,1:实物类商品 默认为1)
        sParaTemp.put("out_trade_no", msgParam.get("orderNo"));          //商户网站唯一订单号
        sParaTemp.put("subject", msgParam.get("goodsName"));               //商品名称
//        sParaTemp.put("total_fee", msgParam.get("total_fee"));         //交易金额
        sParaTemp.put("price", msgParam.get("goodsPrice"));               //商品单价
        sParaTemp.put("quantity", msgParam.get("number"));                //购买数量
        sParaTemp.put("exter_invoke_ip", msgParam.get("exterInvokeIp")); //客户端IP

        // 防钓鱼时间戳
        String anti_phishing_key = "";
        try {
            log.debug("*****开始使用https获取防钓鱼时间戳:");
            if (aliPayConfig.isEnableTimestamp()) { //enable_timestamp
                anti_phishing_key = AlipaySubmit.query_timestamp();
            }
            log.debug("anti_phishing_key=" + anti_phishing_key);
        } catch (Exception e1) {
            e1.printStackTrace();
            log.error("获取防钓鱼时间戳异常：" + e1.getMessage());
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "获取防钓鱼时间戳失败");
        }


        log.info("Alipay 支付请求参数:{}", sParaTemp.toString());
        return AlipaySubmit.buildRequest(sParaTemp, method, "确认");
    }

    /**
     * 即时到账批量退款接口
     *
     * @param msgParam 请求参数
     * @param method   请求方法{get,post}
     * @return html
     */
    private String doRefund(Map<String, String> msgParam, String method) {
        Map<String, String> sParaTemp = new HashMap<>();
        //基本参数
        sParaTemp.put("_input_charset", aliPayConfig.getInputCharset());//编码格式
        sParaTemp.put("service", msgParam.get("service"));
        sParaTemp.put("notify_url", msgParam.get("notifyUrl"));
        sParaTemp.put("partner", msgParam.get("partner"));
        sParaTemp.put("seller_user_id", msgParam.get("partner"));
        sParaTemp.put("refund_date", msgParam.get("refundDate"));
        sParaTemp.put("batch_no", msgParam.get("batchNo"));
        String batchNum = msgParam.get("batchNum");
        sParaTemp.put("batch_num", batchNum);      //退款笔数
        String detailData = msgParam.get("detailData");
        sParaTemp.put("detail_data", detailData);  //退款详细数据 必填(支付宝交易号^退款金额^备注)多笔请用#隔开
        int count = CommonUtils.getCountAppearInString(detailData, "#");
        if (count != Integer.valueOf(batchNum)) {
            log.error("退款笔数[{}]与退款详情中的记录数[{}]不相等", batchNum, count);
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR,
                    "退款笔数 " + batchNum + " 与退款详情中的记录数 " + count + " 不相等");
        }

        log.info("Alipay 退款请求参数:{}", sParaTemp.toString());
        return AlipaySubmit.buildRequest(sParaTemp, method, "确认");
    }

    /**
     * 支付请求参数校验
     *
     * @param msgParam 请求参数
     */
    private void checkPayParams(Map<String, String> msgParam) {
        String goodsId = msgParam.get("goodsId");//商品ID
        if (StringUtils.isEmpty(goodsId)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "商品ID不能为空");
        }
        String goodsName = msgParam.get("goodsName");//商品名称
        if (StringUtils.isEmpty(goodsName)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "商品名称不能为空");
        }
        String goodsPrice = msgParam.get("goodsPrice");//商品单价
        if (StringUtils.isEmpty(goodsPrice)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "商品单价不能为空");
        }
        String buyersid = String.valueOf(msgParam.get("buyerId"));//创建订单的会员ID
        if (StringUtils.isEmpty(buyersid)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "创建订单会员ID不能为空");
        }
        String number = msgParam.get("number");//订单商品数量
        if (StringUtils.isEmpty(number)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "订单商品数量不能为空");
        }

//        String amount = msgParam.get("amount");//订单商品总额
//        if (StringUtils.isEmpty(amount)) {
//            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "订单商品总额不能为空");
//        }
//        String payAmount = msgParam.get("payAmount");//订单交易金额
//        if (StringUtils.isEmpty(payAmount)) {
//            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "订单交易金额不能为空");
//        }
//        String mobile = msgParam.get("mobile");//手机号码
//        if (StringUtils.isEmpty(mobile)) {
//            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "手机号码不能为空");
//        }

        String paymode = msgParam.get("payMode");//支付方式
        if (StringUtils.isEmpty(paymode)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "支付方式不能为空");
        }
        String goodsType = msgParam.get("goodsType");//商品类型
        if (StringUtils.isEmpty(goodsType)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "商品类型不能为空");
        }

        String partner = msgParam.get("partner");// 卖方商家支付宝账号
        if (StringUtils.isEmpty(partner)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "卖方商家支付宝账号不能为空");
        }

//        String returnUrl = msgParam.get("returnUrl");// 同步通知
//        if (StringUtils.isEmpty(returnUrl)) {
//            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR,"同步通知地址不能为空");
//        }

        String notifyUrl = msgParam.get("notifyUrl");// 异步通知
        if (StringUtils.isEmpty(notifyUrl)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "异步通知地址不能为空");
        }

        String exterInvokeIp = msgParam.get("exterInvokeIp");// 客户端IP
        if (StringUtils.isEmpty(exterInvokeIp)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "客户端IP不能为空");
        }

        String service = msgParam.get("service");
        if (StringUtils.isEmpty(service)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "接口名称不能为空");
        }
    }


    /**
     * 获取请求参数
     *
     * @param request http 请求
     * @return map
     * @throws UnsupportedEncodingException
     */
    public Map<String, String> getRequestParams(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Object o : requestParams.keySet()) {
            String name = (String) o;
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
                log.info("param[{}]:{}", i, valueStr);
            }
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        log.info("*********返回参数信息params:" + params + "***********");
        return params;
    }

    /**
     * 返回成功时业务逻辑处理
     *
     * @param params     请求参数
     * @param notifyType 通知类型
     * @return
     */
    public Map<String, String> tradeSuccessDeal(Map<String, String> params, String notifyType, String tradeType) throws ParseException {
        log.info("支付返回成功,业务逻辑处理...");
        Map<String, String> resultMap = new HashMap<>();

        try {

            //1-> 记录支付宝回调信息 交易流水信息
            //订单
            Order order = new Order();
            order.setOrderNo(params.get("out_trade_no"));
            order.setUpdateTime(new Date());
            //异步通知
            if (notifyType.equals(PayConstants.NotifyType.ASYNC.toString())) {
                log.info("异步通知返回记录处理...");
                //即时到账支付
                if (tradeType.equals(PayConstants.TradeType.PAY.toString())) {
                    recordPayAsyncCallbackLog(params);
                    //2-> 修改订单状态为已支付
                    order.setStatus(PayConstants.OrderStatus.YZF.toString());

                }
                //即时到账退款
                if (tradeType.equals(PayConstants.TradeType.REFUND.toString())) {
                    recordRefundAsyncCallbackLog(params);
                    //修改订单状态为已支付
                    order.setStatus(PayConstants.OrderStatus.YTK.toString());
                }
            }
            //同步通知
            if (notifyType.equals(PayConstants.NotifyType.SYNC.toString())) {
                log.info("同步通知返回记录处理...");
            }

            //2. 修改订单状态
            orderService.update(order);

            resultMap.put("statusCode", String.valueOf(PayConstants.HTTP_SUCCESS_CODE));

        } catch (Exception e) {
            e.printStackTrace();
            log.error("支付宝{}回调接口业务处理异常:", e.getMessage());
            resultMap.put("statusCode", String.valueOf(PayConstants.HTTP_SYSTEM_EXCEPTION_CODE));
        }


        return resultMap;
    }

    /**
     * 记录 支付宝即时到账退款接口 异步通知返回记录
     *
     * @param params
     */
    private void recordRefundAsyncCallbackLog(Map<String, String> params) {
        log.info("支付宝即时到账退款接口,异步通知返回记录 入表操作...");
        AlipayRefundCallbackLog refundCallbackLog = new AlipayRefundCallbackLog();
        ConvertUtils.register(new DateConvert(), Date.class);
        Map<String, String> pMap = Maps.newHashMap();
        for (String key : params.keySet()) {
            pMap.put(CommonUtils.getCamelString(key), params.get(key));
        }
        log.info("需转换为bean的pMap=" + pMap);
        try {
            BeanUtils.populate(refundCallbackLog, pMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("支付宝回调参数map转换为bean异常" + e.getMessage());
        }

        refundCallbackLogService.insert(refundCallbackLog);

    }

    /**
     * 记录 支付宝即时到账接口 异步通知返回记录
     *
     * @param params
     */
    private void recordPayAsyncCallbackLog(Map<String, String> params) {
        log.info("支付宝即时到账接口 ,异步通知返回记录 入表操作... ");
        AlipayCallbackLog alipayCallbackLog = new AlipayCallbackLog();
        ConvertUtils.register(new DateConvert(), Date.class);
        Map<String, String> pMap = Maps.newHashMap();
        for (String key : params.keySet()) {
            pMap.put(CommonUtils.getCamelString(key), params.get(key));
        }
        pMap.put("price", String.valueOf(new BigDecimal(pMap.get("price")).multiply(new BigDecimal(1000)).longValue()));
        pMap.put("totalFee", String.valueOf(new BigDecimal(pMap.get("totalFee")).multiply(new BigDecimal(1000)).longValue()));
        log.info("需转换为bean的pMap=" + pMap);
        try {
            BeanUtils.populate(alipayCallbackLog, pMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("支付宝回调参数map转换为bean异常" + e.getMessage());
        }
        alipayCallbackLogService.insert(alipayCallbackLog);
    }
}
