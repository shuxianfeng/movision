package com.zhuhuibao.web.controller;

import com.zhuhuibao.mybatis.entity.User;
import com.zhuhuibao.mybatis.service.UserService;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @since 15/12/12.
 */
@Controller
public class PaginationController {
    private static final Logger logger = LoggerFactory.getLogger(PaginationController.class);
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/pager")
    public String index(HttpServletRequest req,Model model,String pageNo,String pageSize) {
        HttpSession sess = req.getSession(true);
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "5";
        }
        Paging<User> pager = new Paging<User>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        List<User>  users = userService.findAllByPager(pager);
        pager.result(users);
        model.addAttribute("pager",pager);
        return "pagination";
    }
}
