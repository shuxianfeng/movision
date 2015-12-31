package com.zhuhuibao.web.controller;

import com.zhuhuibao.mybatis.entity.User;
import com.zhuhuibao.mybatis.service.UserService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author jianglz
 */
@Controller
public class DemoController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/demo", method = RequestMethod.GET)
    public String showTest(HttpServletRequest req,Model model){
        HttpSession sess = req.getSession(true);
        //获取登陆用户信息
//        ShiroRealm.ShiroUser loginUser = (ShiroRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();


        User user = userService.findByMobile("18652093798");

        model.addAttribute("status",user.getStatus());
        model.addAttribute("name",user.getName());

        List<User> users = userService.findAll();
        model.addAttribute("users",users);

        return "demo";
    }

}
