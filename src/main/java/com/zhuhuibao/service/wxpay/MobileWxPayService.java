package com.zhuhuibao.service.wxpay;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.jdom.JDOMException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mangofactory.swagger.models.Collections;
import com.zhuhuibao.common.constant.WePayConstant;
import com.zhuhuibao.utils.HttpClientUtils;
import com.zhuhuibao.utils.SignUtil;
import com.zhuhuibao.utils.XmlUtil;
import com.zhuhuibao.utils.wxpay.WxpayPropertiesLoader;

@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
@Service
public class MobileWxPayService {
	
	private static final String GET_OPENID_URL = WxpayPropertiesLoader.getPropertyValue("get_openid_url");
	
	private static final String APPID =  WxpayPropertiesLoader.getPropertyValue("app_id");
	
	private static final String SECRET = WxpayPropertiesLoader.getPropertyValue("secret");
	
	private static final String AUTHORIZATION_CODE = WxpayPropertiesLoader.getPropertyValue("authorization_code");
			
	private static final String MCH_ID = WxpayPropertiesLoader.getPropertyValue("mch_id");
	
	private static final String WX_DO_ORDER_URL = WxpayPropertiesLoader.getPropertyValue("wx_do_order_url");
			
	private static final String MD5 = "MD5";
	
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
	
	/**
	 * 调微信统一下单接口，并处理返回数据
	 * @param openid
	 * @param orderid
	 * @param request
	 * @param wei_xin_notify_url
	 * @return
	 */
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
		
		Map<String, String> doOrderResultMap = HttpClientUtils.doPostForXml(WX_DO_ORDER_URL, signParams, "UTF-8");
		
		System.out.println(doOrderResultMap);
		
		/**
		 * 解析结果:返回正确信息
		 * {result=<xml><return_code><![CDATA[SUCCESS]]></return_code>
				<return_msg><![CDATA[OK]]></return_msg>
				<appid><![CDATA[wx5349d63f9159c0bb]]></appid>
				<mch_id><![CDATA[1393755702]]></mch_id>
				<nonce_str><![CDATA[ZvYIcsfD0qeey76c]]></nonce_str>
				<sign><![CDATA[D121BF10B124CFDB65EBCFCFE5C86B2A]]></sign>
				<result_code><![CDATA[SUCCESS]]></result_code>
				<prepay_id><![CDATA[wx201610131433133ac4c0257f0243044464]]></prepay_id>
				<trade_type><![CDATA[JSAPI]]></trade_type>
				</xml>, status=200}
		 */
		Object resultObj = doOrderResultMap.get("result");
		SortedMap<String, String> jsAPIsignParam = new TreeMap<String, String>();
		if(null != resultObj){
			String resultStr = String.valueOf(resultObj);
			Map map ;
			try {
				map = XmlUtil.doXMLParse(resultStr);
				System.out.println("map>>>"+map.toString());
				String return_code=(String) map.get("return_code");	//返回状态码
				String result_code = (String) map.get("result_code");	//业务结果
				String prepay_id =null;
				
				if(return_code.equals("SUCCESS") && result_code.equals("SUCCESS")){
					prepay_id=(String) map.get("prepay_id");//获取到prepay_id
				}
				
				
				long currentTimeMillis = System.currentTimeMillis();//生成时间戳
				long second = currentTimeMillis / 1000L;	//（转换成秒）
				String seconds = String.valueOf(second).substring(0, 10);	//（截取前10位）
				
				jsAPIsignParam.put("appId", APPID);//app_id
				jsAPIsignParam.put("package", "prepay_id="+prepay_id);//默认sign=WXPay
				jsAPIsignParam.put("nonceStr", nonce_str);//自定义不重复的长度不长于32位
				jsAPIsignParam.put("timeStamp",seconds);//北京时间时间戳
				jsAPIsignParam.put("signType", MD5);
				
				String signAgain = SignUtil.createSign("UTF-8", jsAPIsignParam);//再次生成签名
				
				jsAPIsignParam.put("paySign", signAgain);
				
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Map resultMap = new HashMap<>();
		resultMap.put("doOrderResultMap", doOrderResultMap);
		resultMap.put("jsAPIsignParam", jsAPIsignParam);
		return resultMap;
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
