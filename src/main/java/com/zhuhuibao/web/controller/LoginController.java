package com.zhuhuibao.web.controller;

import com.zhuhuibao.utils.captcha.util.CaptchaException;
import com.zhuhuibao.utils.exception.SmsException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 登录
 * @author jianglz
 */
@Controller
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String index(HttpServletRequest req,  Model model) {
        log.debug("登录");
        HttpSession sess = req.getSession(true);

        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest req, Model model) {
        log.info("login post 登录校验");
        HttpSession session = req.getSession(true);
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String rememberMe = req.getParameter("rememberMe");

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        if(rememberMe!=null && rememberMe.equals("1"))
            token.setRememberMe(true);

        Subject currentUser = SecurityUtils.getSubject();

        String result;
        try {
            currentUser.login(token);
            result = "index";
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            model.addAttribute("error", "用户名不存在");
            result = "login";
        }catch (LockedAccountException e){
            e.printStackTrace();
            model.addAttribute("error", "帐户状态异常");
            result = "login";
        } catch (AuthenticationException e){
            e.printStackTrace();
            model.addAttribute("error", "用户名或密码错误");
            result = "login";
        }

        if(currentUser.isAuthenticated()){
            System.out.println("用户[" + username + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
        }else{
            token.clear();
        }
        return result;
    }
}
