package com.zhuhuibao.service.zhpay;

import com.zhuhuibao.alipay.service.direct.AlipayDirectService;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.OrderConstants;
import com.zhuhuibao.common.constant.PayConstants;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.order.entity.*;
import com.zhuhuibao.mybatis.order.service.*;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.service.order.ZHOrderService;
import com.zhuhuibao.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * 支付服务
 */
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
@Service
public class ZhpayService {
    private final static Logger log = LoggerFactory.getLogger(ZhpayService.class);

    @Autowired
    OrderGoodsService orderGoodsService;

    @Autowired
    ZHOrderService zhOrderService;

    @Autowired
    ZhbAccountService zhbAccountService;

    @Autowired
    AlipayDirectService alipayDirectService;

    @Autowired
    OrderFlowService orderFlowService;

    @Autowired
    ZhbService zhbService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PublishCourseService publishCourseService;

    /**
     * 单一方式支付(1.支付宝)
     *
     * @param resp
     * @param msgParam
     * @throws Exception
     */
    public void doPay(HttpServletResponse resp, Map<String, String> msgParam) throws Exception {
        // 记录支付流水
        prePayParam(msgParam);
        // 参数校验
        checkParams(msgParam);
        // 确认支付
        confirmPay(resp, msgParam);

    }

    /**
     * 支付参数准备
     *
     * @param msgParam
     */
    private void prePayParam(Map<String, String> msgParam) {
        // 根据订单编号查询相关信息
        OrderGoods orderGoods = orderGoodsService.findByOrderNo(msgParam.get("orderNo"));
        if (orderGoods != null) {
            Long goodsId = orderGoods.getGoodsId();
            Integer number = orderGoods.getNumber();
            BigDecimal goodPrice = orderGoods.getGoodsPrice(); // 商品单价
            BigDecimal payPrice = goodPrice.multiply(new BigDecimal(number)); // 订单应付金额

            msgParam.put("goodsId", String.valueOf(goodsId));
            msgParam.put("number", String.valueOf(number));

            Map<String, String> flowMap = new HashMap<>();

            // 全部支付宝支付 tradeMode = 1 ( t_o_order_flow )
            // 生成订单支付记录 t_o_order_flow
            flowMap.put("orderNo", msgParam.get("orderNo"));
            flowMap.put("tradeMode", msgParam.get("tradeMode"));
            flowMap.put("price", payPrice.toString());
            zhOrderService.createOrderFlow(flowMap);

        } else {
            log.error("订单不存在");
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "订单不存在");
        }
    }

    /**
     * 支付订单 (组合方式)
     *
     * @param msgParam
     */
    public void doPayMultiple(HttpServletResponse resp, Map<String, String> msgParam) throws Exception {
        // 根据支付方式选择不同的支付
        // 判断支付方式
        judgePayMode(msgParam);
        // 校验参数
        checkParams(msgParam);
        // 确认支付
        confirmPay(resp, msgParam);

    }

    /**
     * 确认支付
     *
     * @param resp
     * @param msgParam
     * @throws Exception
     */
    public void confirmPay(HttpServletResponse resp, Map<String, String> msgParam) throws Exception {

        // 根据t_o_order_flow表进行支付
        List<OrderFlow> orderFlows = orderFlowService.findUniqueOrderFlow(msgParam.get("orderNo"));

        if (orderFlows.size() > 0) {
            for (OrderFlow flow : orderFlows) {
                if (PayConstants.PayMode.ALIPAY.toString().equals(flow.getTradeMode())) { // 支付宝支付
                    // 支付宝支付参数准备
                    msgParam.put("goodsPrice", flow.getTradeFee().toString());
                    preAliPayParams(msgParam);
                    // pc--调用支付宝支付
                    if (null == msgParam.get("payType") || (null != msgParam.get("payType") && msgParam.get("payType").equals(""))
                            || (null != msgParam.get("payType") && msgParam.get("payType").equals("pc"))) {
                        alipayDirectService.doPay(resp, msgParam);
                    } else {
                        // 触屏--调用支付宝支付
                        alipayDirectService.h5Pay(resp, flow, msgParam);
                    }

                } else if (PayConstants.PayMode.ZHBPAY.toString().equals(flow.getTradeMode())) {// 筑慧币支付
                    // 参数准备 校验
                    preZhPayParams(msgParam);
                    // 调用筑慧币支付平台
                    String orderNo = flow.getOrderNo();
                    zhbService.payForOrder(orderNo);
                } else {
                    log.error("不支持的支付方式");
                    throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "不支持的支付方式");
                }

            }

        } else {
            log.error("订单不存在");
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "订单不存在");
        }
    }

    /**
     * 判断支付方式
     *
     * @param msgParam
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void judgePayMode(Map<String, String> msgParam) {

        // 根据订单编号查询相关信息
        OrderGoods orderGoods = orderGoodsService.findByOrderNo(msgParam.get("orderNo"));

        if (orderGoods != null) {

            Long goodsId = orderGoods.getGoodsId();
            Integer number = orderGoods.getNumber();
            BigDecimal goodPrice = orderGoods.getGoodsPrice(); // 商品单价
            BigDecimal payPrice = goodPrice.multiply(new BigDecimal(number)); // 订单应付金额

            msgParam.put("goodsId", String.valueOf(goodsId));
            msgParam.put("number", String.valueOf(number));

            // 用户筑慧币余额
            Long companyId = ShiroUtil.getCompanyID();
            ZhbAccount zhbAccount = zhbAccountService.findByMemberId(companyId);

            Map<String, String> flowMap;

            if (zhbAccount != null) {
                // 有筑慧币 判断筑慧币余额 和 应付金额的大小
                BigDecimal zhbNum = zhbAccount.getAmount();
                int result = payPrice.compareTo(zhbNum);
                if (result == 0 || result == -1) { // 筑慧币余额==应付金额 或者 筑慧币>应付金额
                    // 全部筑慧币方式支付
                    flowMap = new HashMap<>();
                    flowMap.put("orderNo", msgParam.get("orderNo"));
                    flowMap.put("price", payPrice.toString());
                    flowMap.put("tradeMode", PayConstants.PayMode.ZHBPAY.toString());
                    zhOrderService.createOrderFlow(flowMap);
                } else if (result == 1) {// 筑慧币<应付金额

                    // 全额扣除所有筑慧币余额支付
                    flowMap = new HashMap<>();
                    flowMap.put("orderNo", msgParam.get("orderNo"));
                    flowMap.put("price", zhbNum.toString());
                    flowMap.put("tradeMode", PayConstants.PayMode.ZHBPAY.toString());
                    zhOrderService.createOrderFlow(msgParam);

                    // 剩余金额采用支付宝支付
                    BigDecimal alipayPrice = payPrice.subtract(zhbNum);
                    flowMap = new HashMap<>();
                    flowMap.put("orderNo", msgParam.get("orderNo"));
                    flowMap.put("price", alipayPrice.toString());
                    msgParam.put("tradeMode", msgParam.get("tradeMode"));
                    zhOrderService.createOrderFlow(msgParam);

                }

            } else {// 没有筑慧币
                // 全部支付宝支付 tradeMode = 1 ( t_o_order_flow )
                // 生成订单支付记录 t_o_order_flow
                flowMap = new HashMap<>();
                flowMap.put("orderNo", msgParam.get("orderNo"));
                flowMap.put("tradeMode", msgParam.get("tradeMode"));
                flowMap.put("price", payPrice.toString());
                zhOrderService.createOrderFlow(msgParam);

            }

        } else {
            log.error("订单不存在");
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "订单不存在");
        }

    }

    /**
     * 筑慧币支付参数准备
     *
     * @param msgParam
     */
    private void preZhPayParams(Map<String, String> msgParam) {

        // 根据订单编号查询商品信息
        OrderGoods goods = orderGoodsService.findByOrderNo(msgParam.get("orderNo"));
        if (goods == null) {
            log.error("商品不存在");
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "商品不存在");
        }
    }

    /**
     * 支付宝支付参数准备
     *
     * @param msgParam
     */
    private void preAliPayParams(Map<String, String> msgParam) {
        msgParam.put("alipay_goods_type", "0");// 商品类型(0:虚拟类商品,1:实物类商品 默认为1)

        // 根据商品ID查询商品信息
        OrderGoods goods = orderGoodsService.findByOrderNo(msgParam.get("orderNo"));
        if (goods != null) {
            msgParam.put("goodsName", goods.getGoodsName());

        } else {
            log.error("商品不存在");
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "商品不存在");
        }

    }

    /**
     * 参数校验
     *
     * @param msgParam
     */
    public String checkParams(Map<String, String> msgParam) {
        String msg = "";
        // 如果是培训课程类的支付 校验库存是否满足当前支付条件
        Order order = orderService.findByOrderNo(msgParam.get("orderNo"));
        if (null == order) {
            msg = "订单不存在";
            return msg;
        }
        // 当前订单(专家培训，技术培训)下单时间和当前时间超过30分钟 订单状态置为已关闭
        if (order.getGoodsType().equals(OrderConstants.GoodsType.JSPX.toString()) || order.getGoodsType().equals(OrderConstants.GoodsType.ZJPX.toString())) {
            if (order.getDealTime().before(DateUtils.date2Sub(new Date(), Calendar.MINUTE, -30))) {
                // 订单状态改为关闭
                order.setStatus(PayConstants.OrderStatus.CLOSED.toString());
                orderService.update(order);
                msg = "订单已经失效";
                return msg;
            }
            OrderGoods goods = orderGoodsService.findByOrderNo(msgParam.get("orderNo"));
            if (null == goods) {
                msg = "订单商品信息不存在";
                return msg;
            }
            PublishCourse course = publishCourseService.getCourseById(goods.getGoodsId());
            int stockNum = course.getStorageNumber();
            if (goods.getNumber() > stockNum) { // 购买数量大于库存数量
                msg = "剩余名额只有" + stockNum + "，请修改报名人数再进行提交";
                return msg;
            }
        }
        return msg;
    }
}
