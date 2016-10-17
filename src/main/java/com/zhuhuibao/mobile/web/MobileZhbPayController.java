package com.zhuhuibao.mobile.web;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.alipay.util.AlipayPropertiesLoader;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.pojo.CourseOrderReqBean;
import com.zhuhuibao.common.pojo.ZHBOrderReqBean;
import com.zhuhuibao.common.pojo.PayReqBean;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.service.MobileZhbPayService;
import com.zhuhuibao.service.course.CourseService;
import com.zhuhuibao.service.order.ZHOrderService;
import com.zhuhuibao.service.zhpay.ZhpayService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 订单下单支付
 * @author zhuangyuhao
 * @time   2016年10月14日 下午2:46:05
 *
 */
@RestController
@RequestMapping("/rest/m/pay/site")
@Api(value = "MobileZhbOrderPAY", description = "订单下单支付")
public class MobileZhbPayController {

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


    @ApiOperation(value = "筑慧币|VIP购买提交订单", notes = "筑慧币|VIP购买提交订单", response = Response.class)
    @RequestMapping(value = "do_zhb_order", method = RequestMethod.POST)
    public Response doZHBOrder(@ApiParam @ModelAttribute ZHBOrderReqBean order) {
    	
    	//执行下单业务处理，并且返回生成的订单号
    	String orderNo = mobileZhbPaySV.doZHBOrder(order);
    	
        Response response = new Response();
        response.setData(orderNo);
        return response;
    }


    @ApiOperation(value = "培训课程下单", notes = "培训课程下单", response = Response.class)
    @RequestMapping(value = "do_course_order", method = RequestMethod.POST)
    public Response createOrder(@ApiParam @ModelAttribute CourseOrderReqBean order) {
    	
        String orderNo = mobileZhbPaySV.createOrder(order);

        Response response = new Response();
        response.setData(orderNo);
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
}
