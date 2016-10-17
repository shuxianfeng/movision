package com.zhuhuibao.mobile.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;
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
	
//	@ApiOperation(value = "获取调用微信统一下单接口的APPID", notes = "获取调用微信统一下单接口的APPID", response = Response.class)
//    @RequestMapping(value = "getAppId", method = RequestMethod.POST)
//	public Response get(){
//		
//		Response response = new Response();
//        response.setData(appid);
//        return response;
//	}
	
}
