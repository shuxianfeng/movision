package com.zhuhuibao.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author jianglz
 * @since 15/12/12.
 */
@Controller
public class RegisterController {
    private static final Logger log = LoggerFactory.getLogger(RegisterController.class);

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String index(HttpServletRequest req,  Model model) {
        log.debug("注册");
        HttpSession sess = req.getSession(true);
        return "register";
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String register(HttpServletRequest req,  Model model){
        log.debug("注册");
        return "login";
    }
}
