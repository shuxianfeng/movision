package com.zhuhuibao.mobile.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.http.HttpRequest;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.service.wxpay.MobileWxPayService;
import com.zhuhuibao.utils.wxpay.WxpayPropertiesLoader;

/**
 * 手机端
 * 微信支付接口
 * @author zhuangyuhao
 * @time   2016年10月14日 下午3:13:53
 *
 */
@RestController
@RequestMapping("/rest/m/wxpay")
public class MobileWexinPayController {

	private static final Logger log = LoggerFactory.getLogger(MobileAlipayController.class);
	
	@Autowired
	MobileWxPayService mobileWxPaySV;
	
	@ApiOperation(value = "获取调用微信统一下单接口的APPID", notes = "获取调用微信统一下单接口的APPID", response = Response.class)
    @RequestMapping(value = "getAppId", method = RequestMethod.POST)
	public Response getAppId(){
		
		String appid = WxpayPropertiesLoader.getPropertyValue("app_id");
		Response response = new Response();
        response.setData(appid);
        return response;
	}
	
	@ApiOperation(value = "调用微信统一下单接口,获取JSAPI参数", notes = "调用微信统一下单接口,获取JSAPI参数", response = Response.class)
    @RequestMapping(value = "getWxPayJSAPIParams", method = RequestMethod.POST)
	public Response getWxPayJSAPIParams(
			@ApiParam(value = "请求code") @RequestParam(required = true) String code,
			@ApiParam(value = "订单编号") @RequestParam(required = true) String orderid,
			@ApiParam(value = "http请求对象") @RequestParam(required = true) HttpServletRequest request,
			@ApiParam(value = "wei_xin_notify_url") @RequestParam(required = true) String wei_xin_notify_url

			){
		
		String openid = mobileWxPaySV.getOpenId(code);
		Map result = mobileWxPaySV.handleOrder(openid, orderid, request, wei_xin_notify_url);
		Response response = new Response();
        response.setData(result);
        return response;
	}
	
}
