package com.zhuhuibao.alipay.service;

import com.google.common.collect.Maps;
import com.zhuhuibao.alipay.config.AliPayConfig;
import com.zhuhuibao.alipay.util.AlipayNotify;
import com.zhuhuibao.alipay.util.AlipayPropertiesLoader;
import com.zhuhuibao.alipay.util.AlipaySubmit;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.PayConstants;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.order.entity.AlipayCallbackLog;
import com.zhuhuibao.mybatis.order.entity.AlipayRefundCallbackLog;
import com.zhuhuibao.mybatis.order.entity.Order;
import com.zhuhuibao.mybatis.order.entity.OrderGoods;
import com.zhuhuibao.mybatis.order.service.AlipayRefundCallbackLogService;
import com.zhuhuibao.mybatis.order.service.OrderGoodsService;
import com.zhuhuibao.mybatis.order.service.OrderService;
import com.zhuhuibao.mybatis.order.service.AlipayCallbackLogService;
import com.zhuhuibao.utils.CommonUtils;
import com.zhuhuibao.utils.IdGenerator;
import com.zhuhuibao.utils.PropertiesUtils;
import com.zhuhuibao.utils.convert.DateConvert;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝服务入口
 */
@Service
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

    /**
     * 组装基本参数
     *
     * @return paramMap
     */
    private Map<String, String> genBasicParmas() {
        Map<String, String> paramMap = new HashMap<>();

        //partner == seller_id
//        paramMap.put("partner", aliPayConfig.getPartner());
//        paramMap.put("seller_id", aliPayConfig.getPartner());
        paramMap.put("_input_charset", aliPayConfig.getInputCharset());
        paramMap.put("payment_type", aliPayConfig.getPaymentType());

        return paramMap;
    }

    /**
     * 支付包支付请求
     *
     * @param msgParam 请求参数集合
     * @param method   提交方式。两个值可选：post、get
     * @return html
     */
    public String alipay(Map<String, String> msgParam, String method) throws ParseException {


        //生成订单号,交易流水号
        String orderNo = IdGenerator.createOrderNo();
        msgParam.put("orderNo", orderNo);

        //请求参数校验
        checkParams(msgParam);

        //生成一条订单记录
        genOrderRecord(msgParam);

        //支付操作
        return doPay(msgParam, method);
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
                            && String
                            .valueOf(PayConstants.HTTP_SUCCESS_CODE)
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
                    Order order = orderService.findByOrderNo(params.get("out_trade_no"));
                    if (!String.valueOf(order.getPayAmount()).equals(params.get("total_fee"))) {
                        log.error("支付宝返回交易金额[{}] 与 订单支付金额[{}] 不符合", params.get("total_fee"), order.getPayAmount());
                        out.println("fail");
                    }
                    if (!order.getSellerId().equals(params.get("seller_id"))) {
                        log.error("支付宝返回卖家支付宝用户号[{}] 与 订单卖家支付宝用户号[{}] 不符合", params.get("seller_id"), order.getSellerId());
                        out.println("fail");
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
     * 生成一条订单记录
     *
     * @param msgParam 请求参数
     */
    private void genOrderRecord(Map<String, String> msgParam) throws ParseException {
        log.debug("request params:{}", msgParam.toString());
        Order order = new Order();
        order.setOrderNo(msgParam.get("orderNo"));
        order.setBuyerId(Long.valueOf(msgParam.get("buyerId")));
        order.setSellerId(msgParam.get("seller_id"));
        Date dealTime;
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dealTime = sf.parse(msgParam.get("dealTime"));
        order.setDealTime(dealTime);
        order.setAmount(BigDecimal.valueOf(Long.valueOf(msgParam.get("amount")))); //订单总金额
        order.setPayAmount(BigDecimal.valueOf(Long.valueOf(msgParam.get("payAmount"))));  //交易金额
        order.setGoodsType(msgParam.get("goodsType"));
        order.setPayMode(msgParam.get("payMode"));
        order.setStatus(PayConstants.OrderStatus.WZF.toString());

        orderService.insert(order);

        //订单商品
        OrderGoods orderGoods = new OrderGoods();
        orderGoods.setGoodsId(Long.valueOf(msgParam.get("goodsId")));
        orderGoods.setGoodsName(msgParam.get("goodsName"));
        orderGoods.setGoodsPrice(BigDecimal.valueOf(Long.parseLong(msgParam.get("goodsPrice"))));
        orderGoods.setNumber(Integer.valueOf(msgParam.get("number")));
        orderGoods.setOrderNo(msgParam.get("orderNo"));
        orderGoods.setCreateTime(new Date());

        orderGoodsService.insert(orderGoods);

    }


    /**
     * 支付操作
     *
     * @param msgParam 请求参数
     * @param method   请求方法{get,post}
     * @return html
     */
    private String doPay(Map<String, String> msgParam, String method) {

        Map<String, String> basicMap = genBasicParmas();
        Map<String, String> sParaTemp = new HashMap<>();
        sParaTemp.putAll(basicMap);

        //基本参数
        sParaTemp.put("service", msgParam.get("service"));              //接口名称
        sParaTemp.put("notify_url", msgParam.get("notifyUrl"));         //服务器异步通知页面路径
//        sParaTemp.put("return_url", msgParam.get("returnUrl"));       //页面跳转同步通知页面路径
        sParaTemp.put("partner", msgParam.get("seller_id"));            //合作伙伴ID  == 商家支付宝账户号
        sParaTemp.put("seller_id", msgParam.get("seller_id"));          //partner = seller_id

        //业务参数
        sParaTemp.put("out_trade_no", msgParam.get("orderNo"));          //商户网站唯一订单号
        sParaTemp.put("subject", msgParam.get("subject"));               //商品名称
        sParaTemp.put("total_fee", msgParam.get("total_fee"));           //交易金额
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


        log.debug("Alipay 请求参数:{}", sParaTemp.toString());
        return AlipaySubmit.buildRequest(sParaTemp, method, "确认");
    }

    /**
     * 即时到账批量退款接口
     * @param msgParam   请求参数
     * @param method     请求方法{get,post}
     * @return  html
     */
    private String doRefund(Map<String, String> msgParam, String method) {
        return null;
    }

    /**
     * 请求参数校验
     *
     * @param msgParam 请求参数
     */
    private void checkParams(Map<String, String> msgParam) {
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
        String buyersid = msgParam.get("buyersId");//创建订单的会员ID
        if (StringUtils.isEmpty(buyersid)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "创建订单会员ID不能为空");
        }
        String number = msgParam.get("number");//订单商品数量
        if (StringUtils.isEmpty(number)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "订单商品数量不能为空");
        }

        String amount = msgParam.get("amount");//订单商品总额
        if (StringUtils.isEmpty(amount)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "订单商品总额不能为空");
        }

        String mobile = msgParam.get("mobile");//手机号码
        if (StringUtils.isEmpty(mobile)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "手机号码不能为空");
        }


        String paymode = msgParam.get("payMode");//支付方式
        if (StringUtils.isEmpty(paymode)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "支付方式不能为空");
        }
        String goodsType = msgParam.get("goodsType");//商品类型
        if (StringUtils.isEmpty(goodsType)) {
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "商品类型不能为空");
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
        log.debug("支付返回成功,业务逻辑处理...");
        Map<String, String> resultMap = new HashMap<>();

        try {

            //1-> 记录支付宝回调信息 交易流水信息
            //异步通知
            if (notifyType.equals(PayConstants.NotifyType.ASYNC.toString())) {
                log.debug("异步通知返回记录处理...");
                //即时到账支付
                if (tradeType.equals(PayConstants.TradeType.PAY.toString())) {
                    recordPayAsyncCallbackLog(params);
                }
                //即时到账退款
                if (tradeType.equals(PayConstants.TradeType.REFUND.toString())) {
                    recordRefundAsyncCallbackLog(params);
                }
            }
            //同步通知
            if (notifyType.equals(PayConstants.NotifyType.SYNC.toString())) {
                log.debug("同步通知返回记录处理...");
            }


            //2-> 修改订单状态
            Order order = new Order();
            order.setOrderNo(params.get("out_trade_no"));
            order.setStatus(PayConstants.OrderStatus.YZF.toString());
            order.setUpdateTime(new Date());
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
        log.debug("支付宝即时到账退款接口,异步通知返回记录 入表操作...");
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
        log.debug("支付宝即时到账接口 ,异步通知返回记录 入表操作... ");
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
