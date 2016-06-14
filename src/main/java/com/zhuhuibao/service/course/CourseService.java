package com.zhuhuibao.service.course;

import com.zhuhuibao.alipay.service.direct.AlipayDirectService;
import com.zhuhuibao.common.constant.*;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.order.entity.*;
import com.zhuhuibao.mybatis.order.service.*;
import com.zhuhuibao.service.order.ZHOrderService;
import com.zhuhuibao.shiro.realm.OMSRealm;
import com.zhuhuibao.utils.IdGenerator;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.PropertiesUtils;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import com.zhuhuibao.utils.sms.SDKSendSms;
import com.zhuhuibao.zookeeper.DistributedLock;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
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
 * 培训课程开课流程
 */
@Service
public class CourseService {
    private final static Logger log = LoggerFactory.getLogger(CourseService.class);

    public static final String LOCK_NAME = "alipay";

    //开课 {销售中-->待开课}

    //终止课程 {销售中|待开课 --> 已终止}

    //完成课程 {已完成}

    @Autowired
    PublishCourseService courseService;

    @Autowired
    PwdTickerService pwdTickerService;

    @Autowired
    OrderGoodsService orderGoodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderSmsService orderSmsService;

    @Autowired
    OrderFlowService orderFlowService;

    @Autowired
    private PublishCourseService publishCourseService;

    /**
     * 培训课程购买流程
     * 1.选择课程并下订单
     * 2.选择支付方式支付
     */
    @Autowired
    ZHOrderService zhOrderService;

    @Autowired
    ZhbAccountService zhbAccountService;

    @Autowired
    AlipayDirectService alipayDirectService;


    /**
     * 分布式同步锁定 (zookeeper)
     *
     * @param msgParam
     */
    public void createOrder(Map<String, String> msgParam) {
        log.debug("立即支付请求参数:{}", msgParam.toString());
        DistributedLock lock = null;
        try {
            lock = new DistributedLock(LOCK_NAME);
            lock.lock();

            createOrderLock(msgParam);

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, e.getMessage());
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }
    }

    /**
     * 创建新的课程订单
     *
     * @param msgParam 订单参数
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void createOrderLock(Map<String, String> msgParam) throws Exception {

        //参数准备
        preOrderParams(msgParam);

        //库存校验
        checkRepertory(msgParam);

        String orderNo = IdGenerator.createOrderNo();
        msgParam.put("orderNo", orderNo);

        //记录订单SN码 t_o_pwdticket  扣减库存
        if (msgParam.get("goodsType").equals(OrderConstants.GoodsType.JSPX.toString())
                || msgParam.get("goodsType").equals(OrderConstants.GoodsType.ZJPX.toString())) {

            //发票信息
            zhOrderService.genInvoiceRecord(msgParam);

            //根据订单产品数量生成SN码
            zhOrderService.genSNcode(msgParam, msgParam.get("goodsType"));

            //修改课程库存数量
            zhOrderService.updateSubRepertory(msgParam);

        }

        //生成订单号 创建订单
        zhOrderService.createOrder(msgParam);

    }


    /**
     * 单一方式支付(1.支付宝)
     *
     * @param resp
     * @param msgParam
     * @throws Exception
     */
    public void doPay(HttpServletResponse resp, Map<String, String> msgParam) throws Exception {

        //记录支付流水
        prePayParam(msgParam);

        //确认支付
        confirmPay(resp, msgParam);

    }

    /**
     * 支付参数准备
     *
     * @param msgParam
     */
    private void prePayParam(Map<String, String> msgParam) {
        //根据订单编号查询相关信息
        OrderGoods orderGoods = orderGoodsService.findByOrderNo(msgParam.get("orderNo"));
        if (orderGoods != null) {
            Long goodsId = orderGoods.getGoodsId();
            Integer number = orderGoods.getNumber();
            BigDecimal goodPrice = orderGoods.getGoodsPrice();  //商品单价
            BigDecimal payPrice = goodPrice.multiply(new BigDecimal(number)); //订单应付金额

            msgParam.put("goodsId", String.valueOf(goodsId));
            msgParam.put("number", String.valueOf(number));

            Map<String, String> flowMap = new HashMap<>();

            //全部支付宝支付 tradeMode = 1 ( t_o_order_flow )
            //生成订单支付记录 t_o_order_flow
            flowMap.put("orderNo", msgParam.get("orderNo"));
            flowMap.put("tradeMode", msgParam.get("tradeMode"));
            flowMap.put("price", payPrice.toString());
            zhOrderService.createOrderFlow(msgParam);

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
        //根据支付方式选择不同的支付
        //判断支付方式
        judgePayMode(msgParam);
        //确认支付
        confirmPay(resp, msgParam);

    }


    /**
     * 确认支付
     *
     * @param resp
     * @param msgParam
     * @throws Exception
     */
    private void confirmPay(HttpServletResponse resp, Map<String, String> msgParam) throws Exception {

        //校验参数
        checkParams(msgParam);

        //根据t_o_order_flow表进行支付
        List<OrderFlow> orderFlows = orderFlowService.findByOrderNo(msgParam.get("orderNo"));

        if (orderFlows.size() > 0) {
            for (OrderFlow flow : orderFlows) {
                if (PayConstants.PayMode.ALIPAY.toString().equals(flow.getTradeMode())) { //支付宝支付
                    //支付宝支付参数准备
                    preAliPayParams(msgParam);
                    //调用支付宝支付
                    alipayDirectService.doPay(resp, msgParam);

                } else if (PayConstants.PayMode.ZHBPAY.toString().equals(flow.getTradeMode())) {//筑慧币支付
                    //参数准备
                    preZhPayParams(msgParam);
                    //调用筑慧币支付平台
//                    zhbPayService.doPay(msgParam);
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
    private void judgePayMode(Map<String, String> msgParam) {

        //根据订单编号查询相关信息
        OrderGoods orderGoods = orderGoodsService.findByOrderNo(msgParam.get("orderNo"));

        if (orderGoods != null) {

            Long goodsId = orderGoods.getGoodsId();
            Integer number = orderGoods.getNumber();
            BigDecimal goodPrice = orderGoods.getGoodsPrice();  //商品单价
            BigDecimal payPrice = goodPrice.multiply(new BigDecimal(number)); //订单应付金额

            msgParam.put("goodsId", String.valueOf(goodsId));
            msgParam.put("number", String.valueOf(number));

            //用户筑慧币余额
            Long companyId = ShiroUtil.getCompanyID();
            ZhbAccount zhbAccount = zhbAccountService.findByMemberId(companyId);

            Map<String, String> flowMap;

            if (zhbAccount != null) {
                //有筑慧币  判断筑慧币余额 和 应付金额的大小
                BigDecimal zhbNum = zhbAccount.geteMoney();
                int result = payPrice.compareTo(zhbNum);
                if (result == 0 || result == -1) { //筑慧币余额==应付金额  或者   筑慧币>应付金额
                    //全部筑慧币方式支付
                    flowMap = new HashMap<>();
                    flowMap.put("orderNo", msgParam.get("orderNo"));
                    flowMap.put("price", payPrice.toString());
                    flowMap.put("tradeMode", PayConstants.PayMode.ZHBPAY.toString());
                    zhOrderService.createOrderFlow(flowMap);
                } else if (result == 1) {//筑慧币<应付金额

                    //全额扣除所有筑慧币余额支付
                    flowMap = new HashMap<>();
                    flowMap.put("orderNo", msgParam.get("orderNo"));
                    flowMap.put("price", zhbNum.toString());
                    flowMap.put("tradeMode", PayConstants.PayMode.ZHBPAY.toString());
                    zhOrderService.createOrderFlow(msgParam);

                    //剩余金额采用支付宝支付
                    BigDecimal alipayPrice = payPrice.subtract(zhbNum);
                    flowMap = new HashMap<>();
                    flowMap.put("orderNo", msgParam.get("orderNo"));
                    flowMap.put("price", alipayPrice.toString());
                    msgParam.put("tradeMode", msgParam.get("tradeMode"));
                    zhOrderService.createOrderFlow(msgParam);

                }


            } else {//没有筑慧币
                //全部支付宝支付 tradeMode = 1 ( t_o_order_flow )
                //生成订单支付记录 t_o_order_flow
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
     * 订单参数准备
     *
     * @param msgParam
     */
    private void preOrderParams(Map<String, String> msgParam) {
        //根据商品ID查询商品信息
        Long courseId = Long.valueOf(msgParam.get("goodsId"));
        PublishCourse publishCourse = publishCourseService.getCourseById(courseId);
        if (publishCourse != null) {
            msgParam.put("goodsName", publishCourse.getTitle());
            msgParam.put("goodsPrice", publishCourse.getPrice().toString());

        } else {
            log.error("商品不存在");
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR, "商品不存在");
        }

    }

    /**
     * 筑慧币支付参数准备
     *
     * @param msgParam
     */
    private void preZhPayParams(Map<String, String> msgParam) {

        //根据商品ID查询商品信息
        Long courseId = Long.valueOf(msgParam.get("goodsId"));
        PublishCourse publishCourse = publishCourseService.getCourseById(courseId);
        if (publishCourse != null) {
            msgParam.put("goodsName", publishCourse.getTitle());

        } else {
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
        msgParam.put("alipay_goods_type", "0");//商品类型(0:虚拟类商品,1:实物类商品 默认为1)

        //根据商品ID查询商品信息
        Long courseId = Long.valueOf(msgParam.get("goodsId"));
        PublishCourse publishCourse = publishCourseService.getCourseById(courseId);
        if (publishCourse != null) {
            msgParam.put("goodsName", publishCourse.getTitle());

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
    private void checkParams(Map<String, String> msgParam) {
        String goodsId = msgParam.get("goodsId");//商品ID
        if (StringUtils.isEmpty(goodsId)) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "商品ID不能为空");
        }
        String goodsName = msgParam.get("goodsName");//商品名称
        if (StringUtils.isEmpty(goodsName)) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "商品名称不能为空");
        }
        String goodsPrice = msgParam.get("goodsPrice");//商品单价
        if (StringUtils.isEmpty(goodsPrice)) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "商品单价不能为空");
        }
        String buyersid = String.valueOf(msgParam.get("buyerId"));//创建订单的会员ID
        if (StringUtils.isEmpty(buyersid)) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "创建订单会员ID不能为空");
        }
        String number = msgParam.get("number");//订单商品数量
        if (StringUtils.isEmpty(number)) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "订单商品数量不能为空");
        }
        String goodsType = msgParam.get("goodsType");//商品类型
        if (StringUtils.isEmpty(goodsType)) {
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "商品类型不能为空");
        }

    }


    /**
     * 校验库存
     *
     * @param msgParam
     * @throws Exception
     */
    private void checkRepertory(Map<String, String> msgParam) {
        //技术培训 专家培训购买
        //需要判断购买数量是否>=产品剩余数量
        if (msgParam.get("goodsType").equals(OrderConstants.GoodsType.JSPX.toString()) ||
                msgParam.get("goodsType").equals(OrderConstants.GoodsType.ZJPX.toString())) {
            int number = Integer.valueOf(msgParam.get("number"));
            //获取当前产品库存数量
            Long courseId = Long.valueOf(msgParam.get("goodsId"));
            PublishCourse course = publishCourseService.getCourseById(courseId);
            msgParam.put("goodsPrice", course.getPrice().toString());
            msgParam.put("goodsName", course.getTitle());

            int stockNum = course.getStorageNumber();

            if (number > stockNum) { //购买数量大于库存数量
                log.error("库存不足.购买数量[{}]大于库存数量[{}]", number, stockNum);
                throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "库存不足");
            }
        }

    }

    /**
     * 开课
     *
     * @param courseId 课程ID
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void begin(String courseId) throws Exception {
        PublishCourse course = courseService.getCourseById(Long.valueOf(courseId));

        if (TechConstant.PublishCourseStatus.SALING.toString().equals(String.valueOf(course.getStatus()))) {
            //销售中状态 -- > 待开课
            courseService.updateStatus(course.getCourseid(), TechConstant.PublishCourseStatus.PRECLASS.toString());
            //发送短信告知用户开课相关信息和SN码 {修改SN码状态  未生效-->有效}
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(false);
            OMSRealm.ShiroOmsUser user = (OMSRealm.ShiroOmsUser) session.getAttribute("oms");
            if (user == null) {
                log.error("用户未登陆");
                throw new AuthException(MsgCodeConstant.un_login,
                        MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }
            pwdTickerService.updateStatusByCourseId(course.getCourseid(), TechConstant.SNStatus.EFFECT.toString(),
                    user.getId());


            //查询该课程相关订单(已支付)
            List<Order> orderList = orderService.findListByCourseIdAndStatus(courseId, PayConstants.OrderStatus.YZF.toString());
            //给用户发送短信
            sendSMS(orderList, PropertiesUtils.getValue("course_begin_sms_template_code"));


        } else {
            //非销售中状态 无法开课
            log.error("非[销售中]状态无法开课");
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "非[销售中]状态无法开课");
        }

    }

    /**
     * 终止课程
     *
     * @param courseId 课程ID
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void stop(String courseId) throws Exception {
        //手动终止课程
        //1.销售中状态终止课程
        //2.待开课状态终止课程
        PublishCourse course = courseService.getCourseById(Long.valueOf(courseId));
        if (TechConstant.PublishCourseStatus.SALING.toString().equals(String.valueOf(course.getStatus())) ||
                TechConstant.PublishCourseStatus.PRECLASS.toString().equals(String.valueOf(course.getStatus()))) {  //销售中
            //产品状态{销售中--> 已终止} {待开课--> 已终止}
            courseService.updateStatus(course.getCourseid(), TechConstant.PublishCourseStatus.STOP.toString());

            //SN码状态{未生效->已取消}  {有效->已取消}
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(false);
            OMSRealm.ShiroOmsUser user = (OMSRealm.ShiroOmsUser) session.getAttribute("oms");
            if (user == null) {
                log.error("用户未登陆");
                throw new AuthException(MsgCodeConstant.un_login,
                        MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }
            pwdTickerService.updateStatusByCourseId(course.getCourseid(), TechConstant.SNStatus.CANCELED.toString(),
                    user.getId());

            //对已支付的订单进行退款操作
            //根据课程ID查询已支付的订单 t_o_order_goods
            List<String> orderNoList = orderGoodsService.findListByGoodsId(courseId);
            //修改订单状态 {已支付-->退款中} 进入退款环节
            orderService.batchUpdateStatus(orderNoList, PayConstants.OrderStatus.TKZ.toString());
            //短信通知会员 课程已终止 会全额退款  之后进入退款流程
            //查询该课程相关订单(已支付)
            List<Order> orderList = orderService.findListByCourseIdAndStatus(courseId, PayConstants.OrderStatus.YZF.toString());
            if (TechConstant.PublishCourseStatus.SALING.toString().equals(String.valueOf(course.getStatus()))) {
                //销售中人工终止
                sendSMS(orderList, PropertiesUtils.getValue("course_before_stop_sms_template_code"));
            } else if (TechConstant.PublishCourseStatus.PRECLASS.toString().equals(String.valueOf(course.getStatus()))) {
                //待开课人工终止
                sendSMS(orderList, PropertiesUtils.getValue("course_before_autostop_sms_template_code"));
            }


        } else {
            log.error("非[销售中,待开课]状态无法终止课程");
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "非[销售中,待开课]状态无法终止课程");
        }
    }


    /**
     * 完成课程
     *
     * @param courseId 课程ID
     */
    public void complete(String courseId) {
        PublishCourse course = courseService.getCourseById(Long.valueOf(courseId));

        //课程结束  (手动|系统)     上课中-->完成
        if (TechConstant.PublishCourseStatus.CLASSING.toString().equals(String.valueOf(course.getStatus()))) {
            courseService.updateStatus(course.getCourseid(), TechConstant.PublishCourseStatus.FINISH.toString());

        } else {
            log.error("非[上课中]状态无法完成课程");
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "非[上课中]状态无法完成课程");
        }
    }


    /**
     * 发送短信
     *
     * @param orderList 需要发短信的订单
     */
    public void sendSMS(List<Order> orderList, String template) throws Exception {

        List<String> orderNos = new ArrayList<>();
        for (Order order : orderList) {
            //查询相关订单的短信记录(待发送状态)
            orderNos.add(order.getOrderNo());
        }

        //开课通知短信
        List<OrderSms> smsList = orderSmsService.findByOrderNoAndStatusCode(
                orderNos,
                OrderConstants.SmsStatus.WAITING.toString(),
                template);

        for (OrderSms sms : smsList) {
            //发送短信
            sendSms(sms, template);
        }
    }

    /**
     * 发送短息
     *
     * @param sms
     */
    private void sendSms(OrderSms sms, String template) throws Exception {

        boolean suc = SDKSendSms.sendSMS(sms.getMobile(), sms.getContent(), template);
        OrderSms orderSms = new OrderSms();
        orderSms.setOrderNo(sms.getOrderNo());
        if (suc) {//发送成功
            orderSms.setStatus(OrderConstants.SmsStatus.SUCCESS.toString());
            orderSms.setSendTime(new Date());
        } else {
            //设置发送状态为发送失败
            orderSms.setStatus(OrderConstants.SmsStatus.FAIL.toString());
            orderSms.setUpdateTime(new Date());
        }
        orderSmsService.update(orderSms);
    }


}
