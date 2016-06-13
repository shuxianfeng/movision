package com.zhuhuibao.service.course;

import com.zhuhuibao.common.constant.*;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.order.entity.Order;
import com.zhuhuibao.mybatis.order.entity.OrderSms;
import com.zhuhuibao.mybatis.order.entity.PublishCourse;
import com.zhuhuibao.mybatis.order.service.*;
import com.zhuhuibao.shiro.realm.OMSRealm;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.PropertiesUtils;
import com.zhuhuibao.utils.sms.SDKSendSms;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 培训课程开课流程
 */
@Service
public class CourseService {
    private final static Logger log = LoggerFactory.getLogger(CourseService.class);

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


    /**
     * 购买培训课程
     * @param courseId 课程ID
     */
    public void buy(String courseId){


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
