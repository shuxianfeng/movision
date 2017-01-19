package com.movision.facade.sms;

import com.google.gson.Gson;
import com.movision.common.Response;
import com.movision.common.constant.SmsConstants;
import com.movision.utils.DateUtils;
import com.movision.utils.PropertiesUtils;
import com.movision.utils.VerifyCodeUtils;
import com.movision.utils.sms.SDKSendSms;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/19 13:50
 */
@Service
public class smsFacade {

    private static final Logger log = LoggerFactory.getLogger(smsFacade.class);

    /**
     * 生成手机验证码并发送
     *
     * @param mobile
     */
    public Boolean sendMobileVerifyCode(String mobile) {

        Subject currentUser = SecurityUtils.getSubject();   //获取当前对象
        Session sess = currentUser.getSession(true);    //如果当前没有创建Session对象会创建一个

        // 1 生成短信验证码：四位随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4, VerifyCodeUtils.VERIFY_CODES_DIGIT);
        log.debug("verifyCode == " + verifyCode);
        // 2 发送验证码到手机
        String json = convertVerifyMap2Json(verifyCode);
        SDKSendSms.sendSMS(mobile, json, PropertiesUtils.getValue("modify_mobile_sms_template_code"));
        // 3 把生成的手机验证码缓存到session
        sess.setAttribute("mobile_verifycode_" + mobile, verifyCode);

        return true;
    }

    /**
     * 把验证码的map转换成json格式
     *
     * @param verifyCode
     * @return
     */
    private String convertVerifyMap2Json(String verifyCode) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("code", verifyCode);
        map.put("time", SmsConstants.sms_time);
        Gson gson = new Gson();
        String json = gson.toJson(map);
        return json;
    }


}
