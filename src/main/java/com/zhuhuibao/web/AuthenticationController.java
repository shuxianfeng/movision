package com.zhuhuibao.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.memberReg.service.MemberRegService;
import com.zhuhuibao.security.resubmit.TokenHelper;
import com.zhuhuibao.shiro.realm.ShiroRealm.ShiroUser;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录
 *
 * @author caijl@20160303
 */
@RestController
public class AuthenticationController {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private MemberRegService memberService;

    @RequestMapping(value = "/rest/web/authc", method = RequestMethod.GET)
    public Response isLogin() throws IOException {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        Response response = new Response();
        response.setCode(200);
        Map<String, Object> map = new HashMap<String, Object>();

        if (null == session) {
            response.setMsgCode(0);
            response.setMessage("you are rejected!");
            map.put("authorized", false);
        } else {
            ShiroUser principal = (ShiroUser) session.getAttribute("member");
            if (null == principal) {
                response.setMsgCode(0);
                response.setMessage("you are rejected!");
                map.put("authorized", false);
            } else {
                LoginMember member = new LoginMember();
                member.setAccount(principal.getAccount());
                member.setId(principal.getId());
                response.setMsgCode(1);
                response.setMessage("welcome you!");
                map.put("authorized", true);
                map.put("member", member);
            }
        }

        response.setData(map);
        log.debug("caijl:/rest/web/authc is called,msgcode=[" + response.getMsgCode() + "],Message=[" + response.getMessage() + "].");
        return response;

    }


    @RequestMapping(value = "/rest/getToken", method = RequestMethod.GET)
    public Response getToken(HttpServletRequest req, HttpServletResponse rsp) {
        Response result = new Response();
        String token = TokenHelper.setToken(req);
        result.setData(token);
        return result;
    }

    @RequestMapping(value = "/rest/findMemberInfoById", method = RequestMethod.GET)
    public Response findMemberInfoById() throws IOException {
        Response response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (null == session) {
            response.setMessage("you are not login!");
        } else {
            ShiroUser principal = (ShiroUser) session.getAttribute("member");
            if (null != principal) {
                response = memberService.findMemberInfoById(principal.getId());
            }
        }

        return response;
    }

    public static class LoginMember {
        private String account;
        private Long id;
        private int ordercount;
        private int msgcount;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public int getOrdercount() {
            return ordercount;
        }

        public void setOrdercount(int ordercount) {
            this.ordercount = ordercount;
        }

        public int getMsgcount() {
            return msgcount;
        }

        public void setMsgcount(int msgcount) {
            this.msgcount = msgcount;
        }

        /**
         * 重载equals,只计算account;
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            LoginMember other = (LoginMember) obj;

            if (account == null) {
                return false;
            } else if (account.equals(other.account))
                return true;
            return false;
        }
    }
}
