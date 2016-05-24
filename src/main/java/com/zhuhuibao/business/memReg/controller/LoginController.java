package com.zhuhuibao.business.memReg.controller;

import java.io.IOException;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.mybatis.memberReg.entity.Member;
import com.zhuhuibao.mybatis.memberReg.service.MemberRegService;
import com.zhuhuibao.security.EncodeUtil;
import com.zhuhuibao.utils.MsgPropertiesUtils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.session.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录
 */
@RestController
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MemberRegService memberService;
    
    @RequestMapping(value = "/rest/login", method = RequestMethod.POST)
    public Response login(HttpServletRequest req, Member member) throws IOException {
        log.info("login post 登录校验");
        Response response = new Response();
        String username = member.getAccount();
        UsernamePasswordToken token = null;
        Subject currentUser = null;
        try {
	        String pwd = new String(EncodeUtil.decodeBase64(member.getPassword()));
	        member.setPassword(pwd);
	        String rememberMe = req.getParameter("rememberMe");
	        token = new UsernamePasswordToken(username, pwd);
	        if(rememberMe!=null && rememberMe.equals("1"))
	        {
	            token.setRememberMe(true);
	        }
	        currentUser = SecurityUtils.getSubject();
            currentUser.login(token);
            response.setData(username);
        } catch (UnknownAccountException e) {
//            e.printStackTrace();
            response.setCode(400);
            response.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_username_not_exist)));
            response.setMsgCode(MsgCodeConstant.member_mcode_username_not_exist);
            
        }catch (LockedAccountException e){
            /*e.printStackTrace();
            model.addAttribute("error", "帐户状态异常");
            result = "login";*/
		    response.setCode(400);
		    response.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_account_status_exception)));
		    response.setMsgCode(MsgCodeConstant.member_mcode_account_status_exception);
        } catch (AuthenticationException e){
            /*e.printStackTrace();
            model.addAttribute("error", "用户名或密码错误");
            result = "login";*/
        	response.setCode(400);
		    response.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_usernameorpwd_error)));
		    response.setMsgCode(MsgCodeConstant.member_mcode_usernameorpwd_error);
        }

        if(currentUser.isAuthenticated()){
        	Session session = currentUser.getSession();
        	session.setAttribute("member", currentUser.getPrincipal());
            System.out.println("用户[" + username + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
        }else{
            token.clear();
        }

        return response;
    }
    
    @RequestMapping(value = "/rest/logout", method = RequestMethod.GET)
    public Response logout() throws IOException{
        SecurityUtils.getSubject().logout();
        return new Response();
    }
}
