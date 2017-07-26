package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.common.constant.Constants;
import com.movision.common.constant.MsgCodeConstant;
import com.movision.common.util.ShiroUtil;
import com.movision.facade.user.AppRegisterFacade;
import com.movision.facade.user.UserFacade;
import com.movision.mybatis.user.entity.RegisterUser;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.entity.Validateinfo;
import com.movision.shiro.realm.ShiroRealm;
import com.movision.utils.ValidateUtils;
import com.movision.utils.VerifyCodeUtils;
import com.taobao.api.ApiException;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/4 13:58
 */
@RestController
@RequestMapping("app/login")
public class AppLoginController {

    private static Logger log = LoggerFactory.getLogger(AppLoginController.class);

    @Autowired
    private AppRegisterFacade appRegisterFacade;

    @Autowired
    private UserFacade userFacade;

    /**
     * 手机注册账号时发送的验证码
     *
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     * @throws ApiException
     */
    @ApiOperation(value = "手机注册账号时发送的验证码", notes = "手机注册账号时发送的验证码", response = Response.class)
    @RequestMapping(value = {"/get_mobile_code"}, method = RequestMethod.GET)
    public Response getMobileCode(@ApiParam(value = "验证的手机号") @RequestParam String mobile) throws IOException, ApiException {
        log.debug("获得手机验证码  mobile==" + mobile);
        Response response = new Response();
        if (ValidateUtils.isMobile(mobile)) {
            // 生成随机字串
            String verifyCode = VerifyCodeUtils.generateVerifyCode(Constants.CHECK_MOBILE_CODE_SIZE, VerifyCodeUtils.VERIFY_CODES_DIGIT);
            log.info("verifyCode == " + verifyCode);

            appRegisterFacade.sendSms(mobile, verifyCode);

            appRegisterFacade.putValidationInfoToSession(mobile, verifyCode, "r");

            response.setCode(200);
            response.setData(verifyCode);
        } else {
            log.error("手机号不正确！");
            response.setCode(300);
            response.setMessage("请输入正确的手机号！");
        }

        return response;
    }

    @ApiOperation(value = "H5邀请注册页面-发送手机验证码", notes = "H5邀请注册页面-发送手机验证码", response = Response.class)
    @RequestMapping(value = {"/H5_get_mobile_code"}, method = RequestMethod.GET)
    public Response getMobileCodeForH5(@ApiParam(value = "验证的手机号") @RequestParam String mobile) throws IOException, ApiException {
        log.debug("获得手机验证码  mobile==" + mobile);
        Response response = new Response();
        if (ValidateUtils.isMobile(mobile)) {
            //校验该手机号是否注册过
            User appuser = userFacade.queryUserByPhone(mobile);
            //若该手机号已经注册过，则返回408
            if (null != appuser) {
                response.setCode(408);
                response.setMessage("你已经注册过，请下载美番APP直接登录");
                return response;
            }

            // 生成随机字串
            String verifyCode = VerifyCodeUtils.generateVerifyCode(Constants.CHECK_MOBILE_CODE_SIZE, VerifyCodeUtils.VERIFY_CODES_DIGIT);
            log.info("verifyCode == " + verifyCode);

            appRegisterFacade.sendSms(mobile, verifyCode);

            appRegisterFacade.putValidationInfoToSession(mobile, verifyCode, "r");

            response.setCode(200);
            response.setData(verifyCode);
        } else {
            log.error("手机号不正确！");
            response.setCode(300);
            response.setMessage("请输入正确的手机号！");
        }

        return response;
    }


    /**
     * 验证码校验之后：
     * 1 返回生成的token，保存在app端
     * 2 同时把token放入缓存和数据库
     *
     * @param user
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "手机短信验证码注册", notes = "手机短信验证码注册", response = Response.class)
    @RequestMapping(value = {"/registe_by_phone_sms_code"}, method = RequestMethod.POST)
    public Response registeByPhoneSMS(@ApiParam(value = "会员信息") @ModelAttribute RegisterUser user) throws Exception {

        log.debug("登录信息  mobile==" + user.getPhone() + "mobileCheckCode = " + user.getMobileCheckCode());
        Response response = new Response();
        try {

            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(true);
            if (null == session) {
                log.warn("获取session为null");
                response.setCode(400);
                response.setMessage("请重新获取验证码");
                return response;
            }
            String session_phone = (String) session.getAttribute("phone");
            log.debug("session_phone:" + session_phone);
            String param_phone = user.getPhone();
            log.debug("param_phone:" + param_phone);

            //验证输入错误的手机号正确的验证码登录
            if (!session_phone.equals(param_phone)) {
                response.setCode(400);
                response.setMessage("请输入正确的手机号码");
                return response;
            }
            //此分支是为了6月11号做的H5邀请注册页面
            if (StringUtils.isNotBlank(user.getReferrals())) {
                //有邀请码
                User appuser = userFacade.queryUserByPhone(user.getPhone());
                //若该手机号已经注册过，则返回408
                if (null != appuser) {
                    response.setCode(408);
                    response.setMessage("你已经注册过，请下载美番APP直接登录");
                    return response;
                }
            }

            //校验验证码是否正确
            if (user.getMobileCheckCode() != null) {
                //获取缓存中的登录的用户信息
                Validateinfo validateinfo = (Validateinfo) session.getAttribute("r" + param_phone);
                log.info("【短信验证码登录】获取缓存中的登录的用户信息:" + validateinfo.toString());
                if (null == validateinfo) {
                    response.setCode(400);
                    response.setMessage("session中无当前用户");
                }
                //业务操作
                Map result = appRegisterFacade.validateLoginUser(user, validateinfo, session);
                response.setData(result);
            } else {
                response.setCode(400);
                response.setMessage("请输入手机验证码");
            }
        } catch (Exception e) {
            log.error("注册操作失败>>>", e);
            throw e;
        }

        return response;
    }

    @ApiOperation(value = "注册QQ/微信/微博账号", notes = "注册QQ/微信/微博账号", response = Response.class)
    @RequestMapping(value = {"/registe_by_third_account"}, method = RequestMethod.POST)
    public Response registeByThirdAccount(@ApiParam(value = "第三方登录方式标示。1:QQ， 2:微信， 3:微博 ") @RequestParam Integer flag,
                                          @ApiParam(value = "QQ号/微信号/微博号(填对应的openid)") @RequestParam String account,
                                          @ApiParam(value = "唯一标示：openid") @RequestParam String openid,
                                          @ApiParam(value = "当前设备号") @RequestParam String deviceno,
                                          @ApiParam(value = "第三方账号的头像url") @RequestParam String url,
                                          @ApiParam(value = "第三方账号的昵称") @RequestParam String nickname,
                                          @ApiParam(value = "第三方账号的性别,1男 0女") @RequestParam String sex) throws Exception {
        if (flag == 1) {
            log.debug("【QQ注册】");
        } else if (flag == 2) {
            log.debug("【微信注册】");
        } else {
            log.debug("【微博注册】");
        }
        log.debug("注册qq账号信息：  account==" + account + ", deviceno = " + deviceno + ", openid = " + openid);
        Response response = new Response();
        try {
            Map result = appRegisterFacade.registerQQAccount(flag, account, openid, deviceno, url, nickname, sex);
            response.setData(result);
        } catch (Exception e) {
            log.error("注册操作失败>>>", e);
            throw e;
        }
        return response;
    }

    @ApiOperation(value = "第三方平台账号登录（QQ/Weixin/Weibo）", notes = "第三方平台账号登录（QQ/Weixin/Weibo）", response = Response.class)
    @RequestMapping(value = {"/login_by_third_account"}, method = RequestMethod.POST)
    public Response loginByThirdAccont(@ApiParam(value = "第三方登录方式标示。1:QQ， 2:微信， 3:微博 ") @RequestParam Integer flag,
                                       @ApiParam(value = "QQ/weixin/weibo（填对应的openid）") @RequestParam String account,
                                       @ApiParam(value = "token") @RequestParam String appToken) throws Exception {

        Response response = new Response();
        try {
            User originUser = appRegisterFacade.queryExistThirdAccountAppUser(flag, account);
            if (null == originUser) {
                log.warn("qq账号不存在,请先注册");

                response.setCode(400);
                response.setMessage("qq账号不存在,请先注册");
                response.setMsgCode(MsgCodeConstant.app_account_by_qq_not_exist);
            } else {
                appRegisterFacade.handleLoginProcess(appToken, response, originUser);
            }

        } catch (Exception e) {
            log.error("登录操作失败>>>", e);
            throw e;
        }
        return response;
    }

    /**
     * 该接口用于app端的自动登录，打开app第一个调的接口就是这个
     * 1 若app用户未注册，则会报异常：请发送短信验证码登录
     * 2 若app用户已注册，即已存在，则开始比较appToken和serverToken,若相同，则进入shiro的认证流程,
     * 若认证成功， 则在session中缓存当前app用户，并且删除session中的boss用户，
     * <p>
     *
     * PS：这里有个问题，如果一个用户既登录app，又登录boss，怎么办？
     * --其实他们的session不同，所以逻辑没有问题，
     * 因为同一个session当前登录人只能是app用户或者是boss用户的一个。
     *
     * @param phone
     * @param appToken
     * @return 返回给前台IM注册的信息
     * @throws Exception
     */
    @ApiOperation(value = "通过手机号登录", notes = "通过手机号登录", response = Response.class)
    @RequestMapping(value = {"/login_by_phone"}, method = RequestMethod.POST)
    public Response loginByPhone(@ApiParam(value = "手机号") @RequestParam String phone,
                                 @ApiParam(value = "token") @RequestParam String appToken) throws Exception {
        Response response = new Response();
        try {
            //1 校验手机号是否存在
            User user = userFacade.queryUserByPhone(phone);
            if (null == user) {
                //库中无该用户，需要发送短信验证码
                log.warn("手机号不存在,请发送短信验证码登录");

                response.setCode(400);
                response.setMessage("手机号不存在,请发送短信验证码登录");
                response.setMsgCode(MsgCodeConstant.app_user_not_exist);
            } else {
                appRegisterFacade.handleLoginProcess(appToken, response, user);
            }

        } catch (Exception e) {
            log.error("登录操作失败>>>", e);
            throw e;
        }
        return response;
    }


    @ApiOperation(value = "判断app用户是否已经登录", notes = "判断app用户是否已经登录", response = Response.class)
    @RequestMapping(value = "/authc", method = RequestMethod.GET)
    public Response isLogin() throws IOException {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession(false);
        Response response = new Response();
        response.setCode(200);
        Map<String, Object> map = new HashMap<String, Object>();

        if (null == session) {
            response.setMsgCode(0);
            response.setMessage("you are rejected!");
            map.put("authorized", false);
        } else {
            ShiroRealm.ShiroUser appuser = ShiroUtil.getAppUser();
            if (null == appuser) {
                response.setMsgCode(0);
                response.setMessage("you are rejected!");
                map.put("authorized", false);
            } else {
                response.setMsgCode(1);
                response.setMessage("welcome you!");
                map.put("authorized", true);

                map.put("user", appuser);
            }
            log.info("【调用app/login/authc接口，返回的appuser】：" + appuser);
        }
        response.setData(map);
        log.debug("/app/login/authc is called,msgcode=[" + response.getMsgCode() + "],Message=[" + response.getMessage() + "].");
        return response;
    }

    @ApiOperation(value = "退出登录", notes = "退出登录", response = Response.class)
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public Response logout() throws IOException {
        SecurityUtils.getSubject().logout();
        return new Response();
    }

    @ApiOperation(value = "新设备打开APP绑定ACCID", notes = "新设备打开APP绑定ACCID", response = Response.class)
    @RequestMapping(value = "/new_device_binding_accid", method = RequestMethod.POST)
    public Response newDeviceBindingAccid(@ApiParam(value = "设备号") @RequestParam String deviceid) throws IOException {
        Response response = new Response();

        response.setData(appRegisterFacade.registerImDevice(deviceid, response));

        return response;
    }

    @ApiOperation(value = "获取邀请人的信息", notes = "获取邀请人的信息", response = Response.class)
    @RequestMapping(value = {"/get_invite_userinfo"}, method = RequestMethod.POST)
    public Response getInviteUserInfo(@ApiParam(value = "邀请人的userid") @RequestParam Integer inviteId) throws Exception {

        log.debug("邀请人的userid=" + inviteId);
        Response response = new Response();
        User user = userFacade.selectByPrimaryKey(inviteId);
        response.setData(user);

        return response;
    }

    @ApiOperation(value = "验证手机号", notes = "验证手机号", response = Response.class)
    @RequestMapping(value = {"/verifyCellPhoneNumber"}, method = RequestMethod.POST)
    public Response verifyCellPhoneNumber(@ApiParam(value = "手机号") @RequestParam String phone) {
        Response response = new Response();
        String result = AppRegisterFacade.verifyCellPhoneNumber(phone);
        if (response.getCode() == 200) {
            response.setMessage("成功");
        }
        response.setData(result);
        return response;
    }


}
