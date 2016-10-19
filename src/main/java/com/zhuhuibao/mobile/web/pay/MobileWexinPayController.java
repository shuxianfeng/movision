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
	
	@RequestMapping(value = "getWxPayNotify", method = RequestMethod.POST)
	public ModelAndView getWxPayNotify(HttpServletRequest request, HttpServletResponse response) throws JDOMException, IOException, ParseException{
		//交易类型
		String tradeType = PayConstants.TradeType.PAY.toString();
		
		ModelAndView modelAndView = new ModelAndView();
		
		Map requestParams = request.getParameterMap();
		if(null == requestParams){
			throw new BusinessException(MsgCodeConstant.NOTIFY_PARAMS_EMPTY_ERROR, "微信支付结果通用通知接口参数为空");
		}
		Object returnObj = requestParams.get("return");
		if(null == returnObj){
			throw new BusinessException(MsgCodeConstant.NOTIFY_PARAMS_EMPTY_ERROR, "微信支付结果通用通知接口参数为空");
		}
		
		Map requestMap = new HashMap<>();
		requestMap = XmlUtil.doXMLParse(String.valueOf(returnObj));
		if(null == requestMap){
			throw new BusinessException(MsgCodeConstant.NOTIFY_PARAMS_EMPTY_ERROR, "微信支付结果通用通知接口参数为空");
		}
		
		String return_code = (String)requestParams.get("return_code");
		
		if("SUCCESS".equals(return_code)){
			
			String orderno = (String)requestMap.get("out_trade_no");
			//返回成功时业务逻辑处理
            Map<String, String> resultMap = mobileWxPaySV.tradeSuccessDeal(requestMap,
                    PayConstants.NotifyType.SYNC.toString(), tradeType);
            
            log.info("***同步回调：支付平台回调发起方支付方结果：" + resultMap);
			if (resultMap != null
                    && String.valueOf(PayConstants.HTTP_SUCCESS_CODE)
                    .equals(resultMap.get("statusCode"))) {
				
                if ("SUCCESS".equals(resultMap.get("result"))) {
                	
                    modelAndView.addObject("return_code", "SUCCESS");
                    modelAndView.addObject("return_msg", "支付成功");
                }
                
            }
		}else{
			modelAndView.addObject("return_code", "FAIL");
            modelAndView.addObject("return_msg", "支付失败");
		}
		
        return modelAndView; 
	}
	
	
}
