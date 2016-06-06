package com.zhuhuibao.utils.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class SDKSendTaoBaoSMS {

    private static Logger log = LoggerFactory.getLogger(SDKSendTaoBaoSMS.class);

    private static final String TAOBAO_CLIENT_URL = "http://gw.api.taobao.com/router/rest";
    private static final String APPKEY = "23361295";
    private static final String SECRET = "752b6bcb411e07baf34e11e0b4ddb767";


    /**
     * 发送短信
     *
     * @param mobile      短信接收号码 (支持单个或多个手机号码) 英文逗号分隔 最多200个
     * @param params      短信模板参数
     * @param temlateCode 短信模板编码
     * @return 成功失败 {true|false}
     * @throws ApiException
     */
    public static Boolean sendSMS(String mobile, String params, String temlateCode) throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient(TAOBAO_CLIENT_URL, APPKEY, SECRET);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("123456");
        req.setSmsType("normal");
        req.setSmsFreeSignName("筑慧宝");
        req.setSmsParamString(params);
        req.setRecNum(mobile);
        req.setSmsTemplateCode(temlateCode);
        AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
        log.info("send sms response:" + rsp.getBody());
        return rsp.getResult().getSuccess();
    }


    /**
     * * 用户注册时发送的短信验证码    支持，一次最多可提交200个手机号码；（温馨提示：手机号以英文逗号分开）
     *
     * @param mobile    手机号
     * @param checkCode 验证码
     * @param time      过期时间
     * @throws ApiException
     */
    public static void sendRegisterSMS(String mobile, String checkCode, String time) throws ApiException {
        log.info("send register sms mobile =  " + mobile + " checkcode = " + checkCode);
        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "23361295", "752b6bcb411e07baf34e11e0b4ddb767");
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("123456");
        req.setSmsType("normal");
        req.setSmsFreeSignName("筑慧宝");
        req.setSmsParamString("{\"code\":\"" + checkCode + "\",\"time\":\"" + time + "\"}");
        req.setRecNum(mobile);
        req.setSmsTemplateCode("SMS_8440019");
        AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
        System.out.println(rsp.getBody());
    }

    /**
     * 找回密码时发送的短信验证码  一次最多可提交200个手机号码；（温馨提示：手机号以英文逗号分开）
     *
     * @param mobile
     * @param checkCode
     * @param time
     * @throws ApiException
     */
    public static void sendFindPwdSMS(String mobile, String checkCode, String time) throws ApiException {
        log.info("send find pwd sms mobile =  " + mobile + " checkcode = " + checkCode);
        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "23361295", "752b6bcb411e07baf34e11e0b4ddb767");
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("123456");
        req.setSmsType("normal");
        req.setSmsFreeSignName("筑慧宝");
        req.setSmsParamString("{\"code\":\"" + checkCode + "\",\"time\":\"" + time + "\"}");
        req.setRecNum(mobile);
        req.setSmsTemplateCode("SMS_8440021");
        AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
        System.out.println(rsp.getBody());
    }

    /**
     * 修改绑定的手机短信验证码 一次最多可提交200个手机号码；（温馨提示：手机号以英文逗号分开）
     *
     * @param mobile
     * @param checkCode
     * @param time
     * @throws ApiException
     */
    public static void sendModifyBindMobileSMS(String mobile, String checkCode, String time) throws ApiException {
        log.info("send modify bind mobile sms mobile =  " + mobile + " checkcode = " + checkCode);
        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "23361295", "752b6bcb411e07baf34e11e0b4ddb767");
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("123456");
        req.setSmsType("normal");
        req.setSmsFreeSignName("筑慧宝");
        req.setSmsParamString("{\"code\":\"" + checkCode + "\",\"time\":\"" + time + "\"}");
        req.setRecNum(mobile);
        req.setSmsTemplateCode("SMS_8440020");
        AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
        System.out.println(rsp.getBody());
    }

    public static void main(String[] args) throws ApiException {

		/*TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "23326329", "eb3fa5d51db6e7f43cdc2210113f1a1d");
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("123456");
		req.setSmsType("normal");
		req.setSmsFreeSignName("筑慧宝");
		req.setSmsParamString("{\"code\":\"1234\",\"time\":\"10\"}");
		req.setRecNum("15996309704");
		req.setSmsTemplateCode("SMS_6080034");
		AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
		System.out.println(rsp.getBody());*/

        SDKSendTaoBaoSMS.sendFindPwdSMS("13260797061", "1123", "10");
    }

	/**
	 * * 申请专家支持时发送的短信验证码    支持，一次最多可提交200个手机号码；（温馨提示：手机号以英文逗号分开）
	 * @param mobile  手机号
	 * @param checkCode 验证码
	 * @param time  过期时间
	 * @throws ApiException
	 */
	public static void sendExpertSupportSMS(String mobile,String checkCode,String time) throws ApiException
	{
		log.info("send expertSupport sms mobile =  "+mobile+" checkcode = "+checkCode);
		TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "23361295", "752b6bcb411e07baf34e11e0b4ddb767");
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("123456");
		req.setSmsType("normal");
		req.setSmsFreeSignName("筑慧宝");
		req.setSmsParamString("{\"code\":\""+checkCode+"\",\"time\":\""+time+"\"}");
		req.setRecNum(mobile);
		req.setSmsTemplateCode("SMS_8440019");
		AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
		System.out.println(rsp.getBody());
	}

	/**
	 * * 专家培训课程下单发送的短信验证码    支持，一次最多可提交200个手机号码；（温馨提示：手机号以英文逗号分开）
	 * @param mobile  手机号
	 * @param checkCode 验证码
	 * @param time  过期时间
	 * @throws ApiException
	 */
	public static void sendExpertTrainSMS(String mobile,String checkCode,String time) throws ApiException
	{
		log.info("send expertTrain sms mobile =  "+mobile+" checkcode = "+checkCode);
		TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "23361295", "752b6bcb411e07baf34e11e0b4ddb767");
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("123456");
		req.setSmsType("normal");
		req.setSmsFreeSignName("筑慧宝");
		req.setSmsParamString("{\"code\":\""+checkCode+"\",\"time\":\""+time+"\"}");
		req.setRecNum(mobile);
		req.setSmsTemplateCode("SMS_8440019");
		AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
		System.out.println(rsp.getBody());
	}
}
