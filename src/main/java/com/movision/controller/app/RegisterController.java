package com.movision.controller.app;

import com.google.gson.Gson;
import com.movision.common.Response;
import com.movision.common.constant.Constants;
import com.movision.facade.user.AppRegisterFacade;
import com.movision.mybatis.user.entity.RegisterUser;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.entity.Validateinfo;
import com.movision.utils.DateUtils;
import com.movision.utils.PropertiesUtils;
import com.movision.utils.VerifyCodeUtils;
import com.movision.utils.sms.SDKSendSms;
import com.taobao.api.ApiException;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/3 16:39
 */
@RestController
@RequestMapping("app/register")
public class RegisterController {

    private static Logger log = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private AppRegisterFacade appRegisterFacade;

    /**
     * 手机注册账号时发送的验证码
     *
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     * @throws ApiException
     */
    @ApiOperation(value = "手机注册账号时发送的验证码", notes = "手机注册账号时发送的验证码", response = Response.class)
    @RequestMapping(value = {"/getMobileCode"}, method = RequestMethod.GET)
    public Response getMobileCode(@ApiParam(value = "验证的手机号") @RequestParam String mobile) throws IOException, ApiException {
        log.debug("获得手机验证码  mobile==" + mobile);
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(true);
        Response response = new Response();
        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(Constants.CHECK_MOBILE_CODE_SIZE, VerifyCodeUtils.VERIFY_CODES_DIGIT);
        log.debug("verifyCode == " + verifyCode);
        Map<String, String> map = new LinkedHashMap<>();
        map.put("code", verifyCode);
        map.put("time", Constants.sms_time);
        Gson gson = new Gson();
        String json = gson.toJson(map);
        // TODO: 2017/2/3 短信平台未开通 
        SDKSendSms.sendSMS(mobile, json, PropertiesUtils.getValue("register_code_sms_template_code"));
        //验证信息放入session保存
        Validateinfo info = new Validateinfo();
        info.setCreateTime(DateUtils.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        info.setCheckCode(verifyCode);
        info.setAccount(mobile);

        session.setAttribute("r" + mobile, info);

        return response;
    }

    @ApiOperation(value = "会员注册", notes = "会员注册", response = Response.class)
    @RequestMapping(value = {"/register"}, method = RequestMethod.POST)
    public Response register(@ApiParam(value = "会员信息") @ModelAttribute RegisterUser user) throws Exception {
        log.debug("注册  mobile==" + user.getPhone() + "mobileCheckCode = " + user.getMobileCheckCode());
        Response result = new Response();
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(true);
            //校验手机验证码是否正确
            if (user.getMobileCheckCode() != null) {
                Validateinfo validateinfo = (Validateinfo) session.getAttribute("r" + user.getPhone());
                //业务操作
                boolean flag = appRegisterFacade.registerMobileMember(user, validateinfo, session);
                if (flag) {
                    result.setCode(200);
                } else {
                    result.setCode(400);
                }
            }
        } catch (Exception e) {
            log.error("注册操作失败>>>", e);
            throw e;
        }

        return result;
    }
}
