package com.zhuhuibao.service.order;

import com.google.gson.Gson;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.OrderConstants;
import com.zhuhuibao.common.constant.PayConstants;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.order.entity.*;
import com.zhuhuibao.mybatis.order.service.*;
import com.zhuhuibao.utils.IdGenerator;
import com.zhuhuibao.utils.PropertiesUtils;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
            //invoiceService.insert(msgParam);
        }


    }

    /**
     * 检查发票信息参数
     * @param msgParam
     */
    private void checkInvoiceParams(Map<String, String> msgParam) {
        String invoiceType = msgParam.get("invoiceType");
        if (StringUtils.isEmpty(invoiceType)) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "发票类型不能为空");
        }
        String invoiceTitleType = msgParam.get("invoiceTitleType");
        if (StringUtils.isEmpty(invoiceTitleType)) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "发票抬头不能为空");
        }
        String invoicePerson = msgParam.get("invoicePerson");
        if (StringUtils.isEmpty(invoicePerson)) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "发票收件人不能为空");
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
     * 修改课程库存 {减库存}
     *
     * @param params
     */
    public void updateSubRepertory(Map<String, String> params) {
        Long courseId = Long.valueOf(params.get("goodsId"));
        int number = Integer.valueOf(params.get("number"));
        publishCourseService.updateSubStockNum(courseId, number);

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

    //库存返回

}
