package com.zhuhuibao.utils.sms;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cloopen.rest.sdk.CCPRestSmsSDK;

public class SDKSendTemplateSMS {
	
	private static Logger log = LoggerFactory.getLogger(SDKSendTemplateSMS.class);
	
	public static void sendSMS(String mobile,String code)
	{
		HashMap<String, Object> result = null; 
		 CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
		 restAPI.init("app.cloopen.com", "8883");
		 // 初始化服务器地址和端口，沙盒环境配置成sandboxapp.cloopen.com，生产环境配置成app.cloopen.com，端口都是8883. 
		 restAPI.setAccount("8a48b55152a56fc20152eea1041e55f2", "1c51056e0ae74d7f8302d42b42a162e4");
		 // 初始化主账号名称和主账号令牌，登陆云通讯网站后，可在"控制台-应用"中看到开发者主账号ACCOUNT SID和 主账号令牌AUTH TOKEN。
		 restAPI.setAppId("8a48b55152a56fc20152eea1f2d355fa");
		 // 初始化应用ID，如果是在沙盒环境开发，请配置"控制台-应用-测试DEMO"中的APPID。
		 //如切换到生产环境，请使用自己创建应用的APPID
		 result = restAPI.sendTemplateSMS(mobile,"1" ,new String[]{code,"10"});
		 log.info("SDKTestGetSubAccounts result=" + result); 
		if("000000".equals(result.get("statusCode"))){
		 //正常返回输出data包体信息（map）
		HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
	 	Set<String> keySet = data.keySet();
		for(String key:keySet){
			Object object = data.get(key);
			log.info(key +" = "+object);
		}
	 }else{
		 //异常返回输出错误码和错误信息
		 log.info("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
	 }
	}
	
	public static void main(String[] args) {
		SDKSendTemplateSMS.sendSMS("15996309704","1234");
	}

}
