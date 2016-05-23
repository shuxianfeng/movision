package com.zhuhuibao.business.memReg.controller;

import java.io.IOException;

import com.zhuhuibao.common.JsonResult;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录
 */
@Controller
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MemberRegService memberService;
    
    @RequestMapping(value = "/rest/login", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult login(HttpServletRequest req, Member member) throws IOException {
        log.info("login post 登录校验");
        JsonResult jsonResult = new JsonResult();
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
            jsonResult.setData(username);
        } catch (UnknownAccountException e) {
//            e.printStackTrace();
            jsonResult.setCode(400);
            jsonResult.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_username_not_exist)));
            jsonResult.setMsgCode(MsgCodeConstant.member_mcode_username_not_exist);
            
        }catch (LockedAccountException e){
            /*e.printStackTrace();
            model.addAttribute("error", "帐户状态异常");
            result = "login";*/
		    jsonResult.setCode(400);
		    jsonResult.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_account_status_exception)));
		    jsonResult.setMsgCode(MsgCodeConstant.member_mcode_account_status_exception);
        } catch (AuthenticationException e){
            /*e.printStackTrace();
            model.addAttribute("error", "用户名或密码错误");
            result = "login";*/
        	jsonResult.setCode(400);
		    jsonResult.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_usernameorpwd_error)));
		    jsonResult.setMsgCode(MsgCodeConstant.member_mcode_usernameorpwd_error);
        }

        if(currentUser.isAuthenticated()){
        	Session session = currentUser.getSession();
        	session.setAttribute("member", currentUser.getPrincipal());
            System.out.println("用户[" + username + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
        }else{
            token.clear();
        }

        return jsonResult;
    }
    
    @RequestMapping(value = "/rest/logout", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult logout() throws IOException{
        SecurityUtils.getSubject().logout();
        return new JsonResult();
    }
}
