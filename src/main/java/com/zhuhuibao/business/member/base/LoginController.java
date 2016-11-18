package com.zhuhuibao.business.member.base;

import java.io.IOException;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MemberConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.mybatis.memberReg.entity.Member;
import com.zhuhuibao.mybatis.memberReg.service.MemberRegService;
import com.zhuhuibao.security.EncodeUtil;
import com.zhuhuibao.security.resubmit.AvoidDuplicateSubmission;
import com.zhuhuibao.utils.MsgPropertiesUtils;

import com.zhuhuibao.utils.VerifyCodeUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.session.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录
 */
@RestController
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MemberRegService memberService;

    //    @AvoidDuplicateSubmission(removeToken = true)
    @RequestMapping(value = "/rest/login", method = RequestMethod.POST)
    @ApiOperation(value = "登录", notes = "登录", response = Response.class)
    public Response login(HttpServletRequest req, @ApiParam(value = "用户名") @RequestParam String account,
                          @ApiParam(value = "密码（Base64加密）") @RequestParam String password) throws IOException {
        log.debug("login post 登录校验");
        Response response = new Response();

        Member member = new Member();
        member.setAccount(account);

        String pwd = new String(EncodeUtil.decodeBase64(password));
        member.setPassword(pwd);
        //得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        UsernamePasswordToken token = new UsernamePasswordToken(account, pwd);

        Subject currentUser = SecurityUtils.getSubject();
        try {
        	//记住当前用户
            String rememberMe = req.getParameter("rememberMe");
            if (rememberMe != null && rememberMe.equals("1")) {
                token.setRememberMe(true);
            }
            //登录，即身份验证  
            currentUser.login(token);
            response.setData(account);
        } catch (UnknownAccountException e) {
        	//用户名不存在
            response.setCode(400);
            response.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_username_not_exist)));
            response.setMsgCode(MsgCodeConstant.member_mcode_username_not_exist);

        } catch (LockedAccountException e) {
        	//帐户状态异常
            response.setCode(400);
            response.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_account_status_exception)));
            response.setMsgCode(MsgCodeConstant.member_mcode_account_status_exception);
        } catch (AuthenticationException e) {
        	//用户名或密码错误
            response.setCode(400);
            response.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_usernameorpwd_error)));
            response.setMsgCode(MsgCodeConstant.member_mcode_usernameorpwd_error);
        }
        //更新登陆时间
        memberService.updateLoginTime(member.getAccount());
        if (currentUser.isAuthenticated()) {
            Session session = currentUser.getSession();
            session.setAttribute("member", currentUser.getPrincipal());
        } else {
            token.clear();
        }

        return response;
    }


    @ApiOperation(value = "邮箱注册时的图形验证码", notes = "邮箱注册时的图形验证码", response = Response.class)
    @RequestMapping(value = "/rest/loginImgCode", method = RequestMethod.GET)
    public void getLoginImgCode(HttpServletResponse response) throws IOException {
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(false);
        String verifyCode = VerifyCodeUtils.outputHttpVerifyImage(100, 40, response, Constants.CHECK_IMG_CODE_SIZE);
        log.debug("verifyCode == " + verifyCode);
        sess.setAttribute(MemberConstant.SESSION_TYPE_LOGIN, verifyCode);
    }

    @RequestMapping(value = "/rest/logout", method = RequestMethod.GET)
    public Response logout() throws IOException {
        SecurityUtils.getSubject().logout();
        return new Response();
    }
}
