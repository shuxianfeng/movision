package com.zhuhuibao.business.memReg.controller;

import java.io.IOException;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.MsgCodeConstant;
import com.zhuhuibao.mybatis.memberReg.entity.Member;
import com.zhuhuibao.mybatis.memberReg.service.MemberRegService;
import com.zhuhuibao.security.EncodeUtil;
import com.zhuhuibao.utils.JsonUtils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.session.*;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录
 * @author jianglz
 */
@Controller
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MemberRegService memberService;
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String index(HttpServletRequest req,  Model model) {
        log.debug("登录");
        HttpSession sess = req.getSession(true);

        return "login";
    }

    @RequestMapping(value = "/rest/login", method = RequestMethod.POST)
    @ResponseBody
    public void login(HttpServletRequest req,HttpServletResponse response,Member member, Model model) throws JsonGenerationException, JsonMappingException, IOException {
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
            jsonResult.setMessage("用户名不存在");
            jsonResult.setMsgCode(MsgCodeConstant.member_mcode_username_not_exist);
            
        }catch (LockedAccountException e){
            /*e.printStackTrace();
            model.addAttribute("error", "帐户状态异常");
            result = "login";*/
		    jsonResult.setCode(400);
		    jsonResult.setMessage("帐户状态异常");
		    jsonResult.setMsgCode(MsgCodeConstant.member_mcode_account_status_exception);
        } catch (AuthenticationException e){
            /*e.printStackTrace();
            model.addAttribute("error", "用户名或密码错误");
            result = "login";*/
        	jsonResult.setCode(400);
		    jsonResult.setMessage("用户名或密码错误");
		    jsonResult.setMsgCode(MsgCodeConstant.member_mcode_usernameorpwd_error);
        }

        if(currentUser.isAuthenticated()){
        	Session session = currentUser.getSession();
        	session.setAttribute("member", currentUser.getPrincipal());
            System.out.println("用户[" + username + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
        }else{
            token.clear();
        }
        response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }
    
    @RequestMapping(value = "/rest/logout", method = RequestMethod.GET)
    @ResponseBody
    public void logout(HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException
    {
        SecurityUtils.getSubject().logout();
        JsonResult jsonResult = new JsonResult();
        response.setContentType("application/json;charset=utf-8");
      	response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }
}
