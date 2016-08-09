package com.zhuhuibao.alipay.service;

import com.google.common.collect.Maps;
import com.zhuhuibao.alipay.config.AliPayConfig;
import com.zhuhuibao.alipay.util.AlipayNotify;
import com.zhuhuibao.alipay.util.AlipayPropertiesLoader;
import com.zhuhuibao.alipay.util.AlipaySubmit;
import com.zhuhuibao.common.constant.*;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.order.entity.*;
import com.zhuhuibao.mybatis.order.service.*;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 支付宝服务入口
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class AlipayService {
    private static final Logger log = LoggerFactory.getLogger(AlipayService.class);


    @Autowired
    private AliPayConfig aliPayConfig;

    @Autowired
    private OrderService orderService;

    @Autowired
    private AlipayCallbackLogService alipayCallbackLogService;

    @Autowired
    private AlipayRefundCallbackLogService refundCallbackLogService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private OrderFlowService orderFlowService;

    @Autowired
    ZhbService zhbService;


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
    public void genRefundRecord(Map<String, String> msgParam) throws ParseException {
        log.debug("request params:{}", msgParam.toString());
        int batchNum = Integer.valueOf(msgParam.get("batchNum"));

        if (batchNum == 1) {  //单笔退款
            Refund refund = new Refund();
            refund.setBatchNo(msgParam.get("batchNo"));
            refund.setOperatorId(Integer.valueOf(msgParam.get("operatorId")));
            refund.setOrderNo(msgParam.get("orderNos"));
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            refund.setRefundDate(sf.parse(msgParam.get("refundDate")));
            refund.setTotalFee(Long.valueOf(msgParam.get("totalFee")));
            //+一个退款理由 (reason)
            refund.setReason(msgParam.get("reason"));
            refundService.insert(refund);
        } else {
            log.error("暂不支持批量退款");
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "暂不支持批量退款");
        }

    }

    /**
     * 退款请求参数校验
     *
     * @param msgParam 请求参数集合
     */
    public void checkRefundParams(Map<String, String> msgParam) {
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
            log.error("获取out对象异常>>>", e);
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
//                        BigDecimal price = new BigDecimal(params.get("price"));
//                        BigDecimal totalFee = price.multiply(new BigDecimal(params.get("quantity")));
                        BigDecimal totalFee = new BigDecimal(params.get("total_fee"));
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
     * 支付操作
     *
     * @param msgParam 请求参数
     * @param method   请求方法{get,post}
     * @return html
     */
    public String alipay(Map<String, String> msgParam, String method) {

        Map<String, String> sParaTemp = new HashMap<>();
        //基本参数
        sParaTemp.put("_input_charset", aliPayConfig.getInputCharset());//编码格式
        sParaTemp.put("payment_type", aliPayConfig.getPaymentType());   //支付类型 ，无需修改
        sParaTemp.put("service", msgParam.get("service"));              //接口名称
        String noticeTag = AlipayPropertiesLoader.getPropertyValue("alipay_back_switch");
        if (noticeTag.equals("sync")) {
            sParaTemp.put("return_url", msgParam.get("returnUrl"));       //页面跳转同步通知页面路径
        } else if (noticeTag.equals("async")) {
            sParaTemp.put("notify_url", msgParam.get("notifyUrl"));         //服务器异步通知页面路径
        }

        sParaTemp.put("partner", msgParam.get("partner"));              //合作伙伴ID  == 商家支付宝账户号
        sParaTemp.put("seller_id", msgParam.get("partner"));            //partner = seller_id

        //业务参数
        sParaTemp.put("goods_type", msgParam.get("alipay_goods_type"));   //商品类型(0:虚拟类商品,1:实物类商品 默认为1)
        sParaTemp.put("out_trade_no", msgParam.get("orderNo"));          //商户网站唯一订单号
        sParaTemp.put("subject", msgParam.get("goodsName"));               //商品名称
        sParaTemp.put("total_fee", msgParam.get("goodsPrice"));         //交易金额
//        sParaTemp.put("price", msgParam.get("goodsPrice"));               //商品单价
//        sParaTemp.put("quantity", msgParam.get("number"));                //购买数量
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
            log.error("获取防钓鱼时间戳异常：" + e1);
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
    public String doRefund(Map<String, String> msgParam, String method) {
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
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
                log.error("异步通知返回记录处理...[{}]", params.get("out_trade_no"));
                callbackNotice(params, tradeType, order);

            }
            //同步通知
            if (notifyType.equals(PayConstants.NotifyType.SYNC.toString())) {
                log.error("同步通知返回记录处理...[{}]", params.get("out_trade_no"));
//                callbackNotice(params, tradeType, order);
            }

            resultMap.put("statusCode", String.valueOf(PayConstants.HTTP_SUCCESS_CODE));

        } catch (Exception e) {
            log.error("支付宝回调接口业务处理异常:", e);
            resultMap.put("statusCode", String.valueOf(PayConstants.HTTP_SYSTEM_EXCEPTION_CODE));
        }


        return resultMap;
    }

    /**
     * 支付回调通知
     *
     * @param params
     * @param tradeType
     * @param order
     */
    private void callbackNotice(Map<String, String> params, String tradeType, Order order) {
        //即时到账支付
        if (tradeType.equals(PayConstants.TradeType.PAY.toString())) {
            recordPayAsyncCallbackLog(params);
            //2-> 判断是否存在筑慧币支付方式
            OrderFlow orderFlow = orderFlowService.findByOrderNoAndTradeMode(params.get("out_trade_no"),
                    PayConstants.PayMode.ZHBPAY.toString());
            if (orderFlow != null) {
                String tradeStatus = orderFlow.getTradeStatus();
                if (tradeStatus.equals(PayConstants.OrderStatus.YZF.toString())) {
                    //3-> 修改订单状态为已支付
                    order.setStatus(PayConstants.OrderStatus.YZF.toString());
                }
            } else {
                //3-> 修改订单状态为已支付
                OrderFlow alFlow = new OrderFlow();
                alFlow.setOrderNo(params.get("out_trade_no"));
                alFlow.setTradeStatus(PayConstants.OrderStatus.YZF.toString());
                alFlow.setTradeTime(new Date());
                alFlow.setUpdateTime(new Date());
                orderFlowService.update(alFlow);
                log.error("修改t_o_order_flow status>>>");
                order.setStatus(PayConstants.OrderStatus.YZF.toString());
            }

            //2. 修改订单状态
            boolean suc = orderService.update(order);
            log.error("update t_o_order status :>>>>" + suc);
            //购买筑慧币,VIP 需要回调
            if (suc) {
                String orderNo = params.get("out_trade_no");
                callbackZhbPay(orderNo);
            } else {
                throw new BusinessException(MsgCodeConstant.PAY_ERROR, "业务处理失败");
            }

        }
        //退款
        if (tradeType.equals(PayConstants.TradeType.REFUND.toString())) {
            recordRefundAsyncCallbackLog(params);
            //修改订单状态为已支付
            order.setStatus(PayConstants.OrderStatus.YTK.toString());

            //2. 修改订单状态
            orderService.update(order);
        }
    }

    /**
     * 回调筑慧币VIP购买
     *
     * @param orderNo
     */
    private void callbackZhbPay(String orderNo) {
        try {

            Order endOrder = orderService.findByOrderNo(orderNo);
            if (endOrder != null) {
                if (endOrder.getGoodsType().equals(OrderConstants.GoodsType.ZHB.toString())) {

                    int result = zhbService.zhbPrepaidByOrder(orderNo);
                    if (result == 0) {
                        throw new BusinessException(MsgCodeConstant.PAY_ERROR, "筑慧币充值失败");
                    }
                } else if (endOrder.getGoodsType().equals(OrderConstants.GoodsType.VIP.toString())) {
                    int result = zhbService.openVipService(orderNo);
                    if (result == 0) {
                        throw new BusinessException(MsgCodeConstant.PAY_ERROR, "VIP购买失败失败");
                    }
                }
            }
        } catch (Exception e) {
            log.error("筑慧币充值失败:", e);
        }
    }

    /**
     * 记录 支付宝即时到账退款接口 异步通知返回记录
     *
     * @param params
     */
    public void recordRefundAsyncCallbackLog(Map<String, String> params) {
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
            log.error("支付宝回调参数map转换为bean异常>>>", e);
        }

        refundCallbackLogService.insert(refundCallbackLog);

    }

    /**
     * 记录 支付宝即时到账接口 异步通知返回记录
     *
     * @param params
     */
    public void recordPayAsyncCallbackLog(Map<String, String> params) {
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

