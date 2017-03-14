package com.movision.controller.app;

import com.google.gson.Gson;
import com.movision.common.Response;
import com.movision.common.constant.Constants;
import com.movision.common.constant.ImConstant;
import com.movision.common.constant.MsgCodeConstant;
import com.movision.common.constant.SessionConstant;
import com.movision.facade.im.ImFacade;
import com.movision.facade.user.AppRegisterFacade;
import com.movision.facade.user.UserFacade;
import com.movision.mybatis.imuser.entity.ImUser;
import com.movision.mybatis.user.entity.RegisterUser;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.entity.Validateinfo;
import com.movision.shiro.realm.ShiroRealm;
import com.movision.utils.DateUtils;
import com.movision.utils.propertiesLoader.MsgPropertiesLoader;
import com.movision.utils.VerifyCodeUtils;
import com.movision.utils.im.CheckSumBuilder;
import com.movision.utils.propertiesLoader.PropertiesLoader;
import com.movision.utils.sms.SDKSendSms;
import com.taobao.api.ApiException;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

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

    @Autowired
    private ImFacade imFacade;


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
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(true);
        Response response = new Response();
        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(Constants.CHECK_MOBILE_CODE_SIZE, VerifyCodeUtils.VERIFY_CODES_DIGIT);
        response.setData(verifyCode);
        log.debug("verifyCode == " + verifyCode);

        Map<String, String> map = new LinkedHashMap<>();
        map.put("code", verifyCode);
        map.put("time", Constants.sms_time);
        Gson gson = new Gson();
        String json = gson.toJson(map);

        SDKSendSms.sendSMS(mobile, json, PropertiesLoader.getValue("login_app_sms_template_code"));

        //验证信息放入session保存
        Validateinfo info = new Validateinfo();
        info.setCreateTime(DateUtils.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        info.setCheckCode(verifyCode);
        info.setAccount(mobile);

        session.setAttribute("r" + mobile, info);

        return response;
    }

    /**
     * 调用此接口时，已经确定该手机号在数据库中不存在
     * 验证码校验之后：
     * 1 返回生成的token，保存在app端
     * 2 同时把token放入缓存和数据库
     *
     * @param user
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "短信验证码登录", notes = "短信验证码登录", response = Response.class)
    @RequestMapping(value = {"/login_by_sms_code"}, method = RequestMethod.POST)
    public Response loginBySmsCode(@ApiParam(value = "会员信息") @ModelAttribute RegisterUser user) throws Exception {

        log.debug("登录信息  mobile==" + user.getPhone() + "mobileCheckCode = " + user.getMobileCheckCode());
        Response response = new Response();
        try {

            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(true);
            //校验手机验证码是否正确
            if (user.getMobileCheckCode() != null) {
                Validateinfo validateinfo = (Validateinfo) session.getAttribute("r" + user.getPhone());
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

    /**
     * 该接口用于app端的自动登录，打开app第一个调的接口就是这个
     * 1 若app用户未注册，则会报异常：请发送短信验证码登录
     * 2 若app用户已注册，即已存在，则开始比较appToken和serverToken,若相同，则进入shiro的认证流程,
     * 若认证成功， 则在session中缓存当前app用户，并且删除session中的boss用户，
     * <p>
     * PS：（这里逻辑有点问题，如果一个用户既登录app，又登录boss，怎么办？
     * --其实他们的session不同，所以逻辑没有问题，
     * 因为同一个session当前登录人只能是app用户或者是boss用户的一个。）
     *
     * @param phone
     * @param appToken
     * @return 返回给前台IM注册的信息
     * @throws Exception
     */
    @ApiOperation(value = "APP登录", notes = "APP登录", response = Response.class)
    @RequestMapping(value = {"/auto_login"}, method = RequestMethod.POST)
    public Response applogin(@ApiParam(value = "手机号") @RequestParam String phone,
                             @ApiParam(value = "token") @RequestParam String appToken) throws Exception {
        Response response = new Response();
        try {
            //1 校验手机号是否存在
            User user = userFacade.queryUserByPhone(phone);
            if (null == user) {
                //库中无该用户，需要发送短信验证码
                log.warn("用户名不存在,请发送短信验证码登录");

                response.setCode(400);
                response.setMessage("用户名不存在,请发送短信验证码登录");
                response.setMsgCode(MsgCodeConstant.app_user_not_exist);
            } else {
                //存在该用户
                //2 校验appToken和serverToken非空
                String serverToken = this.validateAppTokenAndServerToken(appToken, response, user);

                //3 appToken和serverToken比较
                if (serverToken.equalsIgnoreCase(appToken)) {

                    Subject currentUser = SecurityUtils.getSubject();
                    Gson gson = new Gson();
                    UsernamePasswordToken token = gson.fromJson(appToken, UsernamePasswordToken.class);
                    //4 开始进入shiro的认证流程
                    Map returnMap = new HashedMap();
                    this.shiroLogin(phone, response, currentUser, token, returnMap);

                    if (currentUser.isAuthenticated()) {
                        //5 验证通过则在session中缓存登录用户信息
                        Session session = currentUser.getSession();
                        session.setAttribute(SessionConstant.APP_USER, currentUser.getPrincipal());
                        session.removeAttribute(SessionConstant.BOSS_USER);

                        //6 判断该userid是否存在一个im用户，
                        Boolean isExistImUser = imFacade.isExistAPPImuser();
                        if (!isExistImUser) {
                            //若不存在，则注册im用户
                            ImUser imUser = new ImUser();
                            imUser.setAccid(CheckSumBuilder.getAccid(phone));
                            ImUser newImUser = imFacade.AddImUser(imUser);
                            returnMap.put("imuser", newImUser);
                        } else {
                            returnMap.put("imuser", imFacade.getImuserByCurrentAppuser());
                        }

                        response.setData(returnMap);
                    } else {
                        token.clear();
                    }

                } else {
                    log.warn("appToken和serverToken不相等");
                    response.setCode(400);
                    response.setMessage("appToken和serverToken不相等");
                    response.setMsgCode(MsgCodeConstant.app_token_not_equal_server_token);
                }
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
            ShiroRealm.ShiroUser appuser = (ShiroRealm.ShiroUser) session.getAttribute(SessionConstant.APP_USER);
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
        }
        response.setData(map);
        log.debug("/app/login/authc is called,msgcode=[" + response.getMsgCode() + "],Message=[" + response.getMessage() + "].");
        return response;
    }

    @ApiOperation(value = "测试用-获取当前登录人信息", notes = "测试用-获取当前登录人信息", response = Response.class)
    @RequestMapping(value = {"/testGetAppUserInfo"}, method = RequestMethod.POST)
    public Response testGetAppUserInfo(@ApiParam @RequestParam String phone) {
        Response response = new Response();
        response.setData(userFacade.getLoginUserByPhone(phone));
        return response;
    }


    private Map shiroLogin(String phone, Response response, Subject currentUser, UsernamePasswordToken token, Map map) {
        try {
            //登录，即身份验证 , 开始进入shiro的认证流程
            currentUser.login(token);
            log.debug("登录成功");
            response.setMessage("登录成功");
//            response.setData(phone);
            map.put("phone", phone);
            return map;

        } catch (UnknownAccountException e) {
            log.warn("用户名不存在");
            response.setCode(400);
            response.setMessage(MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.app_user_not_exist)));
            response.setMsgCode(MsgCodeConstant.app_user_not_exist);
        } catch (LockedAccountException e) {
            log.warn("帐户状态异常");
            response.setCode(400);
            response.setMessage(MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.app_account_status_error)));
            response.setMsgCode(MsgCodeConstant.app_account_status_error);
        } catch (AuthenticationException e) {
            log.warn("用户名或验证码错误");
            response.setCode(400);
            response.setMessage(MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.app_account_name_error)));
            response.setMsgCode(MsgCodeConstant.app_account_name_error);
        }
        return null;
    }

    /**
     * 校验app_token和Server_token是否都存在
     *
     * @param appToken
     * @param response
     * @param user
     * @return
     */
    private String validateAppTokenAndServerToken(@ApiParam(value = "token") @RequestParam String appToken, Response response, User user) {
        if (StringUtils.isEmpty(appToken)) {
            log.warn("app本地的token丢失");
            response.setCode(400);
            response.setMessage(MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.app_token_missing)));
            response.setMsgCode(MsgCodeConstant.app_token_missing);
        }

        String serverToken = user.getToken();
        if (StringUtils.isEmpty(serverToken)) {
            log.warn("服务器存储的token丢失");
            response.setCode(400);
            response.setMessage(MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.server_token_missing)));
            response.setMsgCode(MsgCodeConstant.server_token_missing);
        }
        return serverToken;
    }


}
