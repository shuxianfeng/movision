package com.zhuhuibao.aop;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jianglz
 * @since 16/7/5.
 */
public class UserAccessInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(UserAccessInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        UserAccess annotation;

        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(UserAccess.class);
        } else {
            return true;
        }

        if (annotation != null) {
            String value = annotation.value();
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession(false);
            ShiroRealm.ShiroUser member;
            if (session != null) {
                member = (ShiroRealm.ShiroUser) session.getAttribute("member");
                int workType = member.getWorkType();
                String identify = member.getIdentify();
                if (value.equals("ADMIN")) {
                    log.debug("需要管理员权限操作");
                    if(!identify.equals("2") && workType==100)   {              //企业用户 && 管理员
                        return true;
                    }else{
                        log.error("需要管理员权限");
                        throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR,"需要管理员权限");
                    }

                } else {
                    log.debug("非管理员权限操作");

                    return true;
                }
            } else {
                log.error("请登录");
                throw new BusinessException(MsgCodeConstant.un_login, "请先登录");
            }
        }

        return true;
    }
}
