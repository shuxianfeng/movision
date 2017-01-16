package com.movision.aop;

import com.movision.shiro.realm.ShiroRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhuangyuhao
 * @since 16/7/8.
 */
public class TimeoutInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession(false);
        ShiroRealm.ShiroUser member;
        if (session != null) {
        	System.err.println("session开始时间》》》"+session.getStartTimestamp());
            System.out.println("session结束时间》》》"+session.getLastAccessTime());
            System.out.println("session超时时间》》》"+session.getTimeout());
            member = (ShiroRealm.ShiroUser) session.getAttribute("member");
            if (member == null) {
                if (request.getHeader("x-requested-with") != null
                        && request.getHeader("x-requested-with")
                        .equalsIgnoreCase("XMLHttpRequest")) {//如果是ajax请求响应头会有，x-requested-with；
                    response.setHeader("sessionstatus", "timeout");//在响应头设置session状态
                    return false;
                }
            }
        }
        return true;
    }
}
