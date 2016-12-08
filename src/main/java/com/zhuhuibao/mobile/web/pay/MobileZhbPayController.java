package com.zhuhuibao.mobile.web.pay;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ExpertConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.pojo.CourseOrderReqBean;
import com.zhuhuibao.common.pojo.PayReqBean;
import com.zhuhuibao.common.pojo.ZHBOrderReqBean;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.expert.service.ExpertService;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.service.course.CourseService;
import com.zhuhuibao.service.order.ZHOrderService;
import com.zhuhuibao.service.zhbPay.MobileZhbPayService;
import com.zhuhuibao.service.zhpay.ZhpayService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 订单下单支付
 *
 * @author zhuangyuhao
 * @time 2016年10月14日 下午2:46:05
 */
@RestController
@RequestMapping("/rest/m/pay/site")
@Api(value = "MobileZhbOrderPAY", description = "订单下单支付")
public class MobileZhbPayController {
    private static final Logger log = LoggerFactory.getLogger(MobileZhbPayController.class);
    @Autowired
    ZhpayService zhpayService;

    @Autowired
    CourseService courseService;

    @Autowired
    ZHOrderService zhOrderService;

    @Autowired
    ZhbService zhbService;

    @Autowired
    MobileZhbPayService mobileZhbPaySV;


    @Autowired
    ExpertService expertService;

    @ApiOperation(value = "筑慧币|VIP购买提交订单", notes = "筑慧币|VIP购买提交订单", response = Response.class)
    @RequestMapping(value = "do_zhb_order", method = RequestMethod.POST)
    public Response doZHBOrder(@ApiParam @ModelAttribute ZHBOrderReqBean order) {

        order.setInvoiceProvince("999");
        order.setInvoiceCity("999");
        order.setInvoiceArea("999");
        //执行下单业务处理，并且返回生成的订单号
        String orderNo = mobileZhbPaySV.doZHBOrder(order);

        Response response = new Response();
        response.setData(orderNo);
        return response;
    }


    @ApiOperation(value = "培训课程下单", notes = "培训课程下单", response = Response.class)
    @RequestMapping(value = "do_course_order", method = RequestMethod.POST)
    public Response createOrder(@ApiParam @ModelAttribute CourseOrderReqBean order,
                                @ApiParam(value = "手机") @RequestParam(required = true) String mobiles,
                                @ApiParam(value = "验证码") @RequestParam(required = true) String code) {
        Response response = new Response();
        try {
            expertService.checkMobileCode(code,mobiles,ExpertConstant.MOBILE_CODE_SESSION_TYPE_SUPPORT);
            String orderNo = mobileZhbPaySV.createOrder(order);
            response.setData(orderNo);
        } catch (Exception e) {
            log.error("sel_support_img_code error! ", e);
            response.setCode(400);
            response.setMessage(e.getMessage());
        }
        return response;
    }


    @ApiOperation(value = "触屏端-下单-手机验证码获取", notes = "触屏端-下单-手机验证码获取", response = Response.class)
    @RequestMapping(value = "get_course_order", method = RequestMethod.GET)
    public Response getExpertSupport(@ApiParam(value = "手机号码") @RequestParam(required = true) String mobile,
                                     @ApiParam(value = "图形验证码") @RequestParam(required = true) String imgCode) {
        Response response = new Response();
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session sess = currentUser.getSession(true);
            String sessImgCode = (String) sess.getAttribute(ExpertConstant.MOBILE_CODE_SESSION_TYPE_SUPPORT);
            response =mobileZhbPaySV.getTrainMobileCode(mobile, ExpertConstant.MOBILE_CODE_SESSION_TYPE_SUPPORT, imgCode, sessImgCode);
        } catch (Exception e) {
            log.error("get_expert_support error! ", e);
        }
        return response;
    }




    @ApiOperation(value = "筑慧币单独支付", notes = "筑慧币单独支付")
    @RequestMapping(value = "do_zhb_pay", method = RequestMethod.POST)
    public Response doZhbPay(@ApiParam("订单号") @RequestParam String orderNo) {

        Response response = new Response();
        try {
            int result = zhbService.payForOrder(orderNo);
            int code = result == 1 ? 200 : 400;
            response.setCode(code);

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.PAY_ERROR, "支付失败");
        }
        return response;
    }

    @ApiOperation(value = "立即支付", notes = "立即支付")
    @RequestMapping(value = "do_pay", method = RequestMethod.POST)
    public void doPay(HttpServletRequest request, HttpServletResponse response,
                      @ApiParam @ModelAttribute PayReqBean pay) throws Exception {

        mobileZhbPaySV.doPay(request, response, pay);
    }

    @ApiOperation(value = "筑慧币业务消费支付", notes = "筑慧币业务消费支付", response = Response.class)
    @RequestMapping(value = "upd_payforgoods", method = RequestMethod.POST)
    public Response payForGoods(@ApiParam(value = "物品ID") @RequestParam Long goodsId,
                                @ApiParam(value = "物品类型") @RequestParam String goodsType) throws Exception {
        Response response = new Response();

        int result = 0;
        try {
            result = zhbService.payForGoods(goodsId, goodsType);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
        }

        response.setData(result);
        response.setCode(1 == result ? 200 : 400);

        return response;
    }

}
