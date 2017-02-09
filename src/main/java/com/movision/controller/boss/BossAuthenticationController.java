package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.security.resubmit.TokenHelper;
import com.movision.shiro.realm.BossRealm;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * boss登录
 *
 * @Author zhuangyuhao
 * @Date 2017/2/3 9:55
 */
@RestController
public class BossAuthenticationController {
    private static final Logger log = LoggerFactory.getLogger(BossAuthenticationController.class);


    @RequestMapping(value = "/boss/authc", method = RequestMethod.GET)
    public Response isLogin() throws IOException {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession(false);
        Response response = new Response();
        response.setCode(200);
        Map<String, Object> map = new HashMap<String, Object>();

        if (null == session) {
            response.setMsgCode(0);
            response.setMessage("you are rejected!");
            map.put("authorized", false);
        } else {
            BossUser bossUser = (BossUser) session.getAttribute("bossuser");
            if (null == bossUser) {
                response.setMsgCode(0);
                response.setMessage("you are rejected!");
                map.put("authorized", false);
            } else {
                LoginUser user = new LoginUser();
                user.setAccount(bossUser.getUsername());
                user.setId(bossUser.getId());
                response.setMsgCode(1);
                response.setMessage("welcome you!");
                map.put("authorized", true);
                map.put("user", user);
            }
        }

        response.setData(map);

        log.debug("/rest/boss/authc is called,msgcode=[" + response.getMsgCode() + "],Message=[" + response.getMessage() + "].");
        return response;

    }


    @RequestMapping(value = "/boss/getToken", method = RequestMethod.GET)
    public Response getToken(HttpServletRequest req) {
        Response result = new Response();
        String token = TokenHelper.setToken(req);
        result.setData(token);
        return result;
    }

    public static class LoginUser {
        private String account;
        private Integer id;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
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
            LoginUser other = (LoginUser) obj;

            if (account == null) {
                return false;
            } else if (account.equals(other.account))
                return true;
            return false;
        }
    }
}
