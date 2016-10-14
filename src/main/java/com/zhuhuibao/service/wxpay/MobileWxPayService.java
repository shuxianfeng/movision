package com.zhuhuibao.service.wxpay;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mangofactory.swagger.models.Collections;
import com.zhuhuibao.common.constant.WePayConstant;
import com.zhuhuibao.utils.HttpClientUtils;
import com.zhuhuibao.utils.SignUtil;
import com.zhuhuibao.utils.wxpay.WxpayPropertiesLoader;

@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
@Service
public class MobileWxPayService {
	
	private static final String GET_OPENID_URL = WxpayPropertiesLoader.getPropertyValue("get_openid_url");
	
	private static final String APPID =  WxpayPropertiesLoader.getPropertyValue("app_id");
	
	private static final String SECRET = WxpayPropertiesLoader.getPropertyValue("secret");
	
	private static final String AUTHORIZATION_CODE = WxpayPropertiesLoader.getPropertyValue("authorization_code");
			
	private static final String MCH_ID = WxpayPropertiesLoader.getPropertyValue("mch_id");
	
//	private static final String wx_		
			
	/**
	 * 获取微信支付中统一下单接口的传参——openID
	 * 
	 * @param code
	 * @return
	 */
	public String getOpenId(String code){
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("appid", APPID);
		map.put("secret", SECRET);
		map.put("code", code);
		map.put("grant_type", AUTHORIZATION_CODE);
		
		String openid = "";
		Map<String, String> resultMap = HttpClientUtils.doGet(GET_OPENID_URL, map, "UTF-8");
		if(null != resultMap && null != resultMap.get("openid")){
			openid = (String)resultMap.get("openid");
		}
		
		return openid;
	}
	
	public Map<String,String> handleOrder(String openid, String orderid, HttpServletRequest request,
			String wei_xin_notify_url){
		//生成随机数
		String nonce_str = SignUtil.generateString(32);
		SortedMap<String, String> signParams = new TreeMap<String, String>();
		
		signParams.put("appid", APPID);
		signParams.put("mch_id", MCH_ID);
//		signParams.put("device_info", "");
		signParams.put("nonce_str", nonce_str);	//随机数算法
		signParams.put("body", "JSAPI支付测试");
//		signParams.put("detail", "");
//		signParams.put("attach", "");
		signParams.put("out_trade_no", orderid);	//商户订单号
		signParams.put("fee_type", "CNY");	//货币类型
		signParams.put("total_fee", "888");	//总金额
		signParams.put("spbill_create_ip", request.getRemoteAddr());	//终端IP
//		signParams.put("time_start", "");
//		signParams.put("time_expire", "");
//		signParams.put("goods_tag", "");
		signParams.put("notify_url", wei_xin_notify_url);	//接收微信支付异步通知回调地址
		signParams.put("trade_type", "JSAPI");
//		signParams.put("product_id", "12235413214070356458058");
//		signParams.put("limit_pay", "");
		signParams.put("openid", openid);	//trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识
		
		String sign = SignUtil.createSign("UTF-8", signParams);//生成签名
		signParams.put("sign", sign);
		signParams.remove("key");//调用统一下单无需key（商户应用密钥）
		
		String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		Map<String, String> resultMap = HttpClientUtils.doPostForXml(url, signParams, "UTF-8");
		
		System.out.println(resultMap);
		
		return null;
	}
	
	/**
	 * 获取微信等支付的APPID
	 * @param token
	 * @return
	 */
	public String getAppID(String token){
		
		
		return null;
	}
}
