package com.movision.controller.app.mine;

import com.movision.common.Response;
import com.movision.common.constant.Constants;
import com.movision.common.util.ShiroUtil;
import com.movision.facade.user.AppRegisterFacade;
import com.movision.facade.user.UserFacade;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.entity.Validateinfo;
import com.movision.utils.VerifyCodeUtils;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zhuangyuhao
 * @Date 2017/4/20 10:38
 */
@RestController
@RequestMapping("app/mine/binding")
public class BindingAccountController {

    private static Logger log = LoggerFactory.getLogger(BindingAccountController.class);

    @Autowired
    private AppRegisterFacade appRegisterFacade;

    @Autowired
    private UserFacade userFacade;

    @ApiOperation(value = "根据手机号获取验证码", notes = "根据手机号获取验证码", response = Response.class)
    @RequestMapping(value = {"/get_msg_code_by_phone"}, method = RequestMethod.GET)
    public Response getMsgCodeByPhone(@ApiParam(value = "验证的手机号") @RequestParam String mobile) {

        Response response = new Response();
        log.debug("获得手机验证码  mobile==" + mobile);
        //先校验手机号是否已经存在
        if (isExistPhone(mobile, response)) return response;

        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(Constants.CHECK_MOBILE_CODE_SIZE, VerifyCodeUtils.VERIFY_CODES_DIGIT);
        log.debug("verifyCode == " + verifyCode);
        appRegisterFacade.sendSms(mobile, verifyCode);
        appRegisterFacade.putValidationInfoToSession(mobile, verifyCode, "bind");
        response.setData(verifyCode);

        return response;
    }


    private boolean isExistPhone(String mobile, Response response) {
        if (userFacade.isExistAccount(mobile)) {
            response.setCode(400);
            response.setMessage("该手机号已经被注册，请绑定其他手机号");
            response.setData(mobile);
            return true;
        }
        return false;
    }

    @ApiOperation(value = "绑定手机号", notes = "绑定手机号", response = Response.class)
    @RequestMapping(value = {"/binding_phone"}, method = RequestMethod.POST)
    public Response bindingPhone(@ApiParam(value = "手机号") @RequestParam String phone,
                                 @ApiParam(value = "短信验证码") @RequestParam String code) throws Exception {

        log.debug("绑定手机号信息  mobile==" + phone + "mobileCheckCode = " + code);
        Response response = new Response();
        //先校验手机号是否已经存在
        if (isExistPhone(phone, response)) return response;

        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(true);
            //校验手机验证码是否正确
            if (code != null) {
                //获取缓存中的登录的用户信息
                Validateinfo validateinfo = (Validateinfo) session.getAttribute("bind" + phone);
                log.info("【短信验证码登录】获取缓存中的用户信息:" + validateinfo.toString());
                if (null == validateinfo) {
                    response.setCode(400);
                    response.setMessage("session中无当前用户");
                }
                //业务操作
                appRegisterFacade.bindPhoneProcess(phone, code, validateinfo, session);
                response.setMessage("绑定成功！");
                response.setData(ShiroUtil.getAppUser());
            } else {
                response.setCode(400);
                response.setMessage("请输入手机验证码");
            }
        } catch (Exception e) {
            log.error("绑定手机失败>>>", e);
            throw e;
        }
        return response;
    }

    @ApiOperation(value = "绑定第三方账号", notes = "绑定第三方账号", response = Response.class)
    @RequestMapping(value = {"/binding_third_account"}, method = RequestMethod.POST)
    public Response bindingThirdAccount(@ApiParam(value = "第三方登录方式标示。1:QQ， 2:微信， 3:微博 ") @RequestParam Integer flag,
                                        @ApiParam(value = "QQ号/微信号/微博号") @RequestParam String account) throws Exception {

        if (flag == 1) {
            log.debug("【绑定QQ】，账号：" + account);
        } else if (flag == 2) {
            log.debug("【绑定微信】，账号：" + account);
        } else {
            log.debug("【绑定微博】, 账号：" + account);
        }
        Response response = new Response();
        //1 校验是否存在该账号
        User user = appRegisterFacade.queryExistThirdAccountAppUser(flag, account);
        if (null == user) {
            //绑定第三方账号
            userFacade.bindThirdAccount(flag, account);
            //更新session信息
            ShiroUtil.updateAppuserThirdAccount(flag, account);

            response.setCode(200);
            response.setMessage("绑定成功！");
        } else {
            //2 若不存在，则绑定该账号；若存在，则提示换其他账号绑定
            response.setCode(400);
            response.setMessage("该账号已经被绑定过，请更换其他账号绑定");
            response.setData(account);
        }

        return response;
    }

    @ApiOperation(value = "重新绑定新手机号", notes = "重新绑定新手机号", response = Response.class)
    @RequestMapping(value = {"/binding_new_phone"}, method = RequestMethod.POST)
    public Response bindingNewPhone(@ApiParam(value = "新手机号") @RequestParam String phone,
                                    @ApiParam(value = "短信验证码") @RequestParam String code) throws Exception {

        log.debug("重新绑定新手机号信息  mobile==" + phone + "mobileCheckCode = " + code);
        Response response = new Response();
        //先校验手机号是否已经存在
        if (isExistPhone(phone, response)) return response;

        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(true);
            //校验手机验证码是否正确
            if (code != null) {
                //获取缓存中的登录的用户信息
                Validateinfo validateinfo = (Validateinfo) session.getAttribute("bind" + phone);
                log.info("【短信验证码登录】获取缓存中的用户信息:" + validateinfo.toString());
                if (null == validateinfo) {
                    response.setCode(400);
                    response.setMessage("session中无当前用户");
                }
                //业务操作
                appRegisterFacade.bindNewPhoneProcess(phone, code, validateinfo, session);

                response.setMessage("重新绑定新手机号成功！");
                response.setData(ShiroUtil.getAppUser());
            } else {
                response.setCode(400);
                response.setMessage("请输入手机验证码");
            }
        } catch (Exception e) {
            log.error("重新绑定新手机号失败>>>", e);
            throw e;
        }
        return response;
    }


}
