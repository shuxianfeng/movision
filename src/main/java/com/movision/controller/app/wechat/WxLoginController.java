package com.movision.controller.app.wechat;

import com.movision.common.Response;
import com.movision.common.constant.Constants;
import com.movision.common.util.ShiroUtil;
import com.movision.facade.user.AppRegisterFacade;
import com.movision.facade.user.UserFacade;
import com.movision.facade.wechat.WechatFacade;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.entity.Validateinfo;
import com.movision.utils.ValidateUtils;
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

import java.io.IOException;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2018/2/1 16:42
 * 小程序注册/登录相关接口
 */
@RestController
@RequestMapping("/app/wechat/")
public class WxLoginController {

    private static Logger log = LoggerFactory.getLogger(WxLoginController.class);

    @Autowired
    private WechatFacade wechatFacade;

    @Autowired
    private AppRegisterFacade appRegisterFacade;

    @Autowired
    private UserFacade userFacade;

    /**
     * 小程序：使用code换unionid接口
     */
    @ApiOperation(value = "使用code换unionid接口", notes = "使用code换unionid接口", response = Response.class)
    @RequestMapping(value = "getUnionidBycode", method = RequestMethod.POST)
    public Response getOpenidBycode(@ApiParam(value = "登录时客户端生成的code") @RequestParam String code){

        Response response = wechatFacade.getOpenidBycode(code);

        return response;
    }

    /**
     * 小程序：小程序登录接口
     */
    @ApiOperation(value = "小程序登录接口", notes = "小程序登录接口", response = Response.class)
    @RequestMapping(value = "getWechatLogin", method = RequestMethod.POST)
    public Response getWechatLogin(@ApiParam(value = "code换取的unionid") @RequestParam String unionid,
                                   @ApiParam(value = "这里和unionid传同一个值") @RequestParam String account,
                                   @ApiParam(value = "注册时服务器返回的token") @RequestParam String appToken){

        Response response  = new Response();
        User originUser = appRegisterFacade.queryExistThirdAccountAppUser(2, account);//flag:2 微信
        wechatFacade.getWechatLogin(unionid, response, originUser, appToken);

        return response;
    }

    /**
     * 小程序：注册接口
     */
    @ApiOperation(value = "小程序注册接口", notes = "小程序注册接口", response = Response.class)
    @RequestMapping(value = "wechatRegister", method = RequestMethod.POST)
    public Response wechatRegister(@ApiParam(value = "code换取的unionid") @RequestParam String unionid,
                                   @ApiParam(value = "微信头像url") @RequestParam String url,
                                   @ApiParam(value = "微信昵称") @RequestParam String nickname,
                                   @ApiParam(value = "微信性别,1男 0女") @RequestParam String sex
                                   ) throws IOException {
        Response response = new Response();
        int flag = 2;//2 表示微信注册
        String account = unionid;//openid
        String deviceno = "wechatDeviceno";//微信小程序闭源封装，无法进行手机端推送业务，所以此处设备号随便写
        try {
            Map result = appRegisterFacade.registerQQAccount(flag, account, unionid, deviceno, url, nickname, sex);
            response.setData(result);
        } catch (Exception e) {
            log.error("注册操作失败>>>", e);
            throw e;
        }
        return response;
    }

    /**
     * 小程序：绑定手机号接口
     */
    @ApiOperation(value = "绑定手机号", notes = "绑定手机号", response = Response.class)
    @RequestMapping(value = {"/binding_phone"}, method = RequestMethod.POST)
    public Response bindingPhone(@ApiParam(value = "手机号") @RequestParam String phone,
                                 @ApiParam(value = "短信验证码") @RequestParam String code,
                                 @ApiParam(value = "登录接口返回的未绑定手机号的userid") @RequestParam String userid) throws Exception {

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
                wechatFacade.bindPhoneProcess(phone, code, validateinfo, session, userid);
                response.setMessage("绑定成功！");
                response.setData(ShiroUtil.getWechatUser());
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

    private boolean isExistPhone(String mobile, Response response) {
        if (userFacade.isExistAccount(mobile)) {
            response.setCode(400);
            response.setMessage("该手机号已经被注册，请绑定其他手机号");
            response.setData(mobile);
            return true;
        }
        return false;
    }

    @ApiOperation(value = "根据手机号获取验证码", notes = "根据手机号获取验证码", response = Response.class)
    @RequestMapping(value = {"/get_code_by_wechat"}, method = RequestMethod.GET)
    public Response getMsgCodeByPhone(@ApiParam(value = "验证的手机号") @RequestParam String mobile) {

        Response response = new Response();
        log.info("获得手机验证码  mobile==" + mobile);
        if (ValidateUtils.isMobile(mobile)) {
            //先校验手机号是否已经存在
            if (isExistPhone(mobile, response)) return response;
            // 生成随机字串
            String verifyCode = VerifyCodeUtils.generateVerifyCode(Constants.CHECK_MOBILE_CODE_SIZE, VerifyCodeUtils.VERIFY_CODES_DIGIT);
            log.debug("verifyCode == " + verifyCode);
            appRegisterFacade.sendSms(mobile, verifyCode);
            appRegisterFacade.putValidationInfoToSession(mobile, verifyCode, "bind");
            response.setCode(200);
            response.setData(verifyCode);
        } else {
            log.error("手机号不正确！");
            response.setCode(300);
            response.setMessage("请输入正确的手机号！");
        }
        return response;
    }
}
