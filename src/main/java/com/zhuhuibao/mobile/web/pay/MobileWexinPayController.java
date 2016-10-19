package com.zhuhuibao.mobile.web.pay;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.aliyuncs.http.HttpRequest;
import com.taobao.api.domain.BizResult;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.aop.LoginAccess;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.PayConstants;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.service.wxpay.MobileWxPayService;
import com.zhuhuibao.utils.XmlUtil;
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
    @RequestMapping(value = "getAppId", method = RequestMethod.GET)
	public Response getAppId(){
		
		String appid = WxpayPropertiesLoader.getPropertyValue("app_id");
		Response response = new Response();
        response.setData(appid);
        return response;
	}
	
	@ApiOperation(value = "调用微信统一下单接口,获取JSAPI参数", notes = "调用微信统一下单接口,获取JSAPI参数", response = Response.class)
    @RequestMapping(value = "getWxPayJSAPIParams", method = RequestMethod.POST)
	@LoginAccess
	public Response getWxPayJSAPIParams(
			@ApiParam(value = "请求code") @RequestParam(required = true) String code,
			@ApiParam(value = "订单编号") @RequestParam(required = true) String orderid,
			HttpServletRequest request
			){
		
		String openid = mobileWxPaySV.getOpenId(code);
		Map result = mobileWxPaySV.handleOrder(openid, orderid, request);
		Response response = new Response();
        response.setData(result);
        return response;
	}
	
	@RequestMapping(value = "getWxPayNotify", method = RequestMethod.POST)
	@LoginAccess
	public ModelAndView getWxPayNotify(HttpServletRequest request) throws JDOMException, IOException, ParseException{
		
		return mobileWxPaySV.handleWxPayNotify(request);
	}
	
	
}
