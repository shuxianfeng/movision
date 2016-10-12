package com.zhuhuibao.test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

import com.zhuhuibao.common.constant.WePayConstant;
import com.zhuhuibao.utils.HttpClientUtils;
import com.zhuhuibao.utils.SignUtil;
import com.zhuhuibao.utils.XmlUtil;

public class WXpayTest extends BaseSpringContext{
	@Test	
	public void unifiedorder(){
		String nonce_str = SignUtil.generateString(32);
		SortedMap<String, String> signParams = new TreeMap<String, String>();
		
		signParams.put("appid", WePayConstant.APPID);
		signParams.put("mch_id", WePayConstant.MCH_ID);
//		signParams.put("device_info", "");
		signParams.put("nonce_str", nonce_str);	//随机数算法
		signParams.put("body", "JSAPI支付测试");
//		signParams.put("detail", "");
//		signParams.put("attach", "");
		signParams.put("out_trade_no", "20150806125346");
		signParams.put("fee_type", "CNY");
		signParams.put("total_fee", "888");
		signParams.put("spbill_create_ip", "192.168.1.114");
//		signParams.put("time_start", "");
//		signParams.put("time_expire", "");
//		signParams.put("goods_tag", "");
		signParams.put("notify_url", WePayConstant.WEI_XIN_NOTIFY_URL);	//接收微信支付异步通知回调地址
		signParams.put("trade_type", "JSAPI");
		signParams.put("product_id", "12235413214070356458058");
//		signParams.put("limit_pay", "");
		signParams.put("openid", "oUpF8uMuAJO_M2pxb1Q9zNjWeS6o");	//trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识
		
		String sign = SignUtil.createSign("UTF-8", signParams);//生成签名
		signParams.put("sign", sign);
		signParams.remove("key");//调用统一下单无需key（商户应用密钥）
		
		String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		Map<String, String> result = HttpClientUtils.doPostForXml(url, signParams, "UTF-8");
		
		System.out.println(result);
		
		/**
		 * 解析结果
		 */
		/*Map map = XmlUtil.doXMLParse(result.toString());
		String return_code=(String) map.get("return_code");
		String prepay_id =null;
		String returnSign=null;
		String returnNonce_str=null;
		//TODO 此处有问题
		if (return_code.contains("SUCCESS")){
		   prepay_id=(String) map.get("prepay_id");//获取到prepay_id
		}
		StringBuffer weiXinVo=new StringBuffer();
		long currentTimeMillis = System.currentTimeMillis();//生成时间戳
		long second = currentTimeMillis / 1000L;	//（转换成秒）
		String seconds = String.valueOf(second).substring(0, 10);	//（截取前10位）
		SortedMap<String, String> signParam = new TreeMap<String, String>();
		signParam.put("appid", WePayConstant.APPID);//app_id
		signParam.put("partnerid", WePayConstant.MCH_ID);//微信商户账号
		signParam.put("prepayid", prepay_id);//预付订单id
		signParam.put("package", "Sign=WXPay");//默认sign=WXPay
		signParam.put("nonce_str", nonce_str);//自定义不重复的长度不长于32位
		signParam.put("timestamp",seconds);//北京时间时间戳
		String signAgain = SignUtil.createSign("UTF-8", signParam);//再次生成签名
		signParams.put("sign", signAgain);
		
		weiXinVo.append("appid=").append(ConstantUtil.APP_ID).append("&partnerid=")
			.append(ConstantUtil.PARTNER).append("&prepayid=").append(prepay_id).append("&package=Sign=WXPay")
			.append("&noncestr=").append(uuid).append("&timestamp=").append(seconds).append("&sign=")
			.append(signAgain);//拼接参数返回给移动端
		
		return weiXinVo.toString();*/
		
	}
}
