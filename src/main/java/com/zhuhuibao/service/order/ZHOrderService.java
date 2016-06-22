package com.zhuhuibao.service.order;

import com.google.gson.Gson;
import com.zhuhuibao.alipay.service.refund.AlipayRefundService;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.OrderConstants;
import com.zhuhuibao.common.constant.PayConstants;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.order.entity.*;
import com.zhuhuibao.mybatis.order.service.*;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.utils.IdGenerator;
import com.zhuhuibao.utils.PropertiesUtils;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 订单流程服务
 */
@Service
public class ZHOrderService {
    private final static Logger logger = LoggerFactory.getLogger(ZHOrderService.class);

    @Autowired
    OrderService orderService;

    @Autowired
    OrderGoodsService orderGoodsService;

    @Autowired
    private PwdTickerService pwdTickerService;

    @Autowired
    private PublishCourseService publishCourseService;

    @Autowired
    private OrderSmsService orderSmsService;

    @Autowired
    private OrderFlowService orderFlowService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private AlipayRefundService alipayRefundService;

    @Autowired
    ZhbService zhbService;

    /**
     * 生成订单
     * (事务管理)
     *
     * @param msgParam
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void createOrder(Map<String, String> msgParam) {

        //记录订单表
        genOrderRecord(msgParam);
        //记录订单商品表
        genOrderGoodsRecord(msgParam);

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
        order.setDealTime(new Date());
        String payPrice = msgParam.get("goodsPrice");
        String number = msgParam.get("number");
        BigDecimal price = new BigDecimal(payPrice);
        BigDecimal num = new BigDecimal(number);
        BigDecimal amount = price.multiply(num);
        order.setAmount(amount); //订单总金额
        order.setPayAmount(amount);  //交易金额
        order.setGoodsType(msgParam.get("goodsType"));
        order.setStatus(PayConstants.OrderStatus.WZF.toString());

        orderService.insert(order);
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
     * 需要发票 生成发票记录
     *
     * @param msgParam
     */
    public void genInvoiceRecord(Map<String, String> msgParam) {
        //交易发票信息
        if ("true".equals(msgParam.get("needInvoice"))) {
            checkInvoiceParams(msgParam);
            //录入发票信息
            logger.debug("录入发票信息~");
            Map<String, String> jsonMap = new HashMap<>();

//            jsonMap.put("createId",String.valueOf(ShiroUtil.getCreateID()));

            jsonMap.put("orderNo", msgParam.get("orderNo"));
            jsonMap.put("invoiceType", msgParam.get("invoiceType"));
            jsonMap.put("invoiceTitle", msgParam.get("invoiceTitle"));
            jsonMap.put("invoiceTitleType", msgParam.get("invoiceTitleType"));
            jsonMap.put("receiveName", msgParam.get("invoiceReceiveName"));
            jsonMap.put("address", msgParam.get("invoiceAddress"));
            jsonMap.put("mobile", msgParam.get("invoiceMobile"));
            jsonMap.put("province", msgParam.get("invoiceProvince"));
            jsonMap.put("city", msgParam.get("invoiceCity"));
            jsonMap.put("area", msgParam.get("invoiceArea"));

            Gson gson = new Gson();
            String jsonParam = gson.toJson(jsonMap);

            invoiceService.insertInvoice(jsonParam);
        }


    }

    /**
     * 检查发票信息参数
     *
     * @param msgParam
     */
    private void checkInvoiceParams(Map<String, String> msgParam) {
        String invoiceType = msgParam.get("invoiceType");
        if (StringUtils.isEmpty(invoiceType)) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "发票类型不能为空");
        }
        String invoiceTitleType = msgParam.get("invoiceTitleType");
        if (StringUtils.isEmpty(invoiceTitleType)) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "发票抬头类型不能为空");
        }
        String invoiceReceiveName = msgParam.get("invoiceReceiveName");
        if (StringUtils.isEmpty(invoiceReceiveName)) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "发票收件人不能为空");
        }
        String invoiceProvince = msgParam.get("invoiceProvince");
        if (StringUtils.isEmpty(invoiceProvince)) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "发票收件地址(省编码)不能为空");
        }
        String invoiceCity = msgParam.get("invoiceCity");
        if (StringUtils.isEmpty(invoiceCity)) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "发票收件地址(市编码)不能为空");
        }
        String invoiceArea = msgParam.get("invoiceArea");
        if (StringUtils.isEmpty(invoiceArea)) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "发票收件地址(区域编码)不能为空");
        }
        String invoiceAddress = msgParam.get("invoiceAddress");
        if (StringUtils.isEmpty(invoiceAddress)) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "发票收件地址不能为空");
        }
        String invoiceMobile = msgParam.get("invoiceMobile");
        if (StringUtils.isEmpty(invoiceMobile)) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "发票收件手机不能为空");
        }
    }


    /**
     * 根据订单生产SN码
     *
     * @param msgParam
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void genSNcode(Map<String, String> msgParam, String type) throws Exception {
        int num = Integer.valueOf(msgParam.get("number"));
        List<PwdTicket> list = new ArrayList<>();
        List<String> snCodeList = new ArrayList<>();
        PwdTicket pwdTicket;
        for (int i = 0; i < num; i++) {
            pwdTicket = new PwdTicket();
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
        smsMap.put("time", format.format(course.getStartTime()));
        smsMap.put("code", codes);
        Gson gson = new Gson();
        OrderSms orderSms = new OrderSms();
        orderSms.setOrderNo(msgParam.get("orderNo"));
        orderSms.setMobile(msgParam.get("mobile"));
        orderSms.setContent(gson.toJson(smsMap));
        orderSms.setStatus(OrderConstants.SmsStatus.WAITING.toString());
        //开课短信
        orderSms.setTemplateCode(PropertiesUtils.getValue("course_begin_sms_template_code"));
        orderSmsService.insert(orderSms);

        //终止课程短信
        smsMap = new LinkedHashMap<>();
        smsMap.put("name", course.getTitle());
        orderSms.setContent(gson.toJson(smsMap));

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
     * 生成订单交易记录
     *
     * @param msgParam
     */
    public void createOrderFlow(Map<String, String> msgParam) {
        OrderFlow orderFlow = new OrderFlow();
        orderFlow.setOrderNo(msgParam.get("orderNo"));
        orderFlow.setTradeMode(msgParam.get("tradeMode"));
        orderFlow.setTradeFee(new BigDecimal(msgParam.get("price")));
        orderFlow.setTradeStatus(PayConstants.OrderStatus.WZF.toString());
        orderFlow.setCreateTime(new Date());
        orderFlowService.insert(orderFlow);
    }


    /**
     * 退款请求
     *
     * @param msgParam
     */
    public void requestRefund(HttpServletResponse resp, Map<String, String> msgParam) throws Exception {

        String orderNo = msgParam.get("orderNo");
        //查询订单状态是否为 [待退款]
        Order order = orderService.findByOrderNo(orderNo);
        if (order.getStatus().equals(PayConstants.OrderStatus.DTK.toString())) {
            //查询该订单下的订单子流水
            List<OrderFlow> orderFlows = orderFlowService.findByOrderNo(orderNo);

            int size = orderFlows.size();
            if (size > 0) {
                if (size == 1) {  //只有一种支付方式

                    onlyOnePayMode(resp, msgParam, orderNo, order, orderFlows);

                } else {
                    //多种支付方式
                    List<String> modeList = new ArrayList<>();
                    for (OrderFlow flow : orderFlows) {
                        modeList.add(flow.getTradeMode());
                    }
                    if (size > 1) {
                        //判断是否有筑慧币支付?
                        if (modeList.contains(PayConstants.PayMode.ZHBPAY.toString())) {
                            //存在筑慧币支付方式
                            hasZHBPayMode(resp, msgParam, orderNo, order, orderFlows);

                        } else { //不存在筑慧币支付方式
                            noZHBPayMode(resp, msgParam, orderNo, order, orderFlows);

                        }

                    }
                }
            } else {
                logger.error("退款的订单流水不存在");
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "退款的订单流水不存在");
            }

        } else {
            logger.error("非[待退款]状态,不支持退款操作");
            throw new BusinessException(MsgCodeConstant.PAY_ERROR, "非[待退款]状态,不支持退款操作");
        }
    }

    /**
     * 存在筑慧币支付方式的退款处理   (多种支付方式)
     *
     * @param resp
     * @param msgParam
     * @param orderNo
     * @param order
     * @param orderFlows
     * @throws Exception
     */
    private void hasZHBPayMode(HttpServletResponse resp, Map<String, String> msgParam, String orderNo, Order order, List<OrderFlow> orderFlows) throws Exception {
        Map<String, OrderFlow> map = new HashMap<>();
        for (OrderFlow flow : orderFlows) {
            String tradeMode = flow.getTradeMode();
            if (tradeMode.equals(PayConstants.PayMode.ALIPAY.toString())) {
                map.put("alipay_order", flow);
            } else if (tradeMode.equals(PayConstants.PayMode.ZHBPAY.toString())) {
                map.put("zhbpay_order", flow);
            }
        }
        //先处理筑慧币退款
        OrderFlow zhbOrderFlow = map.get("zhbpay_order");
        boolean suc = processZHBPayRefund(msgParam, zhbOrderFlow);
        if (suc) {
            //退款成功处理支付宝退款请求
            OrderFlow alipayOrderFlow = map.get("alipay_order");
            processAlipayRefunc(resp, msgParam, orderNo, order, alipayOrderFlow);
        }

    }

    /**
     * 处理支付宝退款请求
     *
     * @param resp
     * @param msgParam
     * @param orderNo
     * @param order
     * @param alipayOrderFlow
     * @throws Exception
     */
    private void processAlipayRefunc(HttpServletResponse resp, Map<String, String> msgParam,
                                     String orderNo, Order order, OrderFlow alipayOrderFlow) throws Exception {
        logger.debug("进入支付宝退款接口");

        String tradeStatus = alipayOrderFlow.getTradeStatus();
        if (tradeStatus.equals(PayConstants.OrderStatus.DTK.toString())) {
            //准备支付宝退款请求参数
            preAlipayRefundParam(msgParam, orderNo, order, alipayOrderFlow);
            alipayRefundService.doRefund(resp, msgParam);
        } else {
            logger.error("非[待退款]状态,不支持退款操作");
            throw new BusinessException(MsgCodeConstant.PAY_ERROR, "非[待退款]状态,不支持退款操作");
        }
    }

    /**
     * 处理筑慧币退款请求
     *
     * @param zhbOrderFlow
     * @return
     */
    private boolean processZHBPayRefund(Map<String, String> msgParam, OrderFlow zhbOrderFlow) {

        String tradeStatus = zhbOrderFlow.getTradeStatus();
        if (tradeStatus.equals(PayConstants.OrderStatus.DTK.toString())) {
            logger.debug("进入筑慧币退款接口");
            try {
                return zhbService.refundBySystem(msgParam.get("OrderNo")) == 1;
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("筑慧币退款失败");
                throw new BusinessException(MsgCodeConstant.PAY_ERROR,"筑慧币退款失败");
            }
        } else {
            logger.error("非[待退款]状态,不支持退款操作");
            throw new BusinessException(MsgCodeConstant.PAY_ERROR, "非[待退款]状态,不支持退款操作");
        }

    }

    /**
     * 不存在筑慧币支付方式退款处理
     *
     * @param resp
     * @param msgParam
     * @param orderNo
     * @param order
     * @param orderFlows
     * @throws Exception
     */
    private void noZHBPayMode(HttpServletResponse resp, Map<String, String> msgParam, String orderNo, Order order, List<OrderFlow> orderFlows) throws Exception {
        for (OrderFlow flow : orderFlows) {
            String tradeStatus = flow.getTradeStatus();
            if (tradeStatus.equals(PayConstants.OrderStatus.DTK.toString())) {
                String tradeMode = flow.getTradeMode();
                if (tradeMode.equals(PayConstants.PayMode.ALIPAY.toString())) {
                    //支付宝退款
                    processAlipayRefunc(resp, msgParam, orderNo, order, flow);

                }//可能扩展的其他支付方式...else if()
                else {
                    logger.error("不存在的支付方式");
                    throw new BusinessException(MsgCodeConstant.PAY_ERROR, "不存在的支付方式");
                }
            } else {
                logger.error("非[待退款]状态,不支持退款操作");
                throw new BusinessException(MsgCodeConstant.PAY_ERROR, "非[待退款]状态,不支持退款操作");
            }

        }
    }

    /**
     * 一种支付方式退款处理
     *
     * @param resp
     * @param msgParam
     * @param orderNo
     * @param order
     * @param orderFlows
     * @throws Exception
     */
    private void onlyOnePayMode(HttpServletResponse resp, Map<String, String> msgParam, String orderNo, Order order, List<OrderFlow> orderFlows) throws Exception {
        if (orderFlows.get(0).getTradeMode().equals(PayConstants.PayMode.ZHBPAY.toString())) {
            //有且只有筑慧币支付方式
            //筑慧币支付方式 调用筑慧宝退款接口
            logger.debug("进入筑慧宝退款接口");
            processZHBPayRefund(msgParam, orderFlows.get(0));
            return;
        } else if (orderFlows.get(0).getTradeMode().equals(PayConstants.PayMode.ZHBPAY.toString())) {
            //有且只有支付宝支付
            processAlipayRefunc(resp, msgParam, orderNo, order, orderFlows.get(0));
            return;
        } else {
            logger.error("不存在的支付方式");
            throw new BusinessException(MsgCodeConstant.PAY_ERROR, "不存在的支付方式");
        }
    }

    /**
     * 准备支付宝退款请求参数
     *
     * @param msgParam
     * @param orderNo
     * @param order
     * @param flow
     */
    private void preAlipayRefundParam(Map<String, String> msgParam, String orderNo, Order order, OrderFlow flow) {
        //拼装请求参数
        //orderNos  operatorId refundDate totalFee batchNum   detailData
        msgParam.put("orderNos", orderNo);
        msgParam.put("batchNum", "1");
        msgParam.put("totalFee", flow.getTradeFee().toString());
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        msgParam.put("refundDate", sf.format(new Date()));
        //detailData 多条   原付款支付宝交易号^退款总金额^退款理由 #  原付款支付宝交易号^退款总金额^退款理由
        String detailData = String.valueOf(order.getBuyerId()) + "^"
                + flow.getTradeFee().toString() + "^" + msgParam.get("reason");
        msgParam.put("detailData", detailData);
    }
}
