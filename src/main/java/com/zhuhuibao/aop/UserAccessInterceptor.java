package com.zhuhuibao.aop;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.shiro.realm.OMSRealm;
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
import java.util.Arrays;
import java.util.List;

/**
 * 用户登录检验拦截器
 * @author jianglz
 * @since 16/7/5.
 */
public class UserAccessInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(UserAccessInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        UserAccess annotation;

        LoginAccess loginAnno;

        OmsLoginAccess omsLoginAnno;

        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(UserAccess.class);
            loginAnno = ((HandlerMethod) handler).getMethodAnnotation(LoginAccess.class);
            omsLoginAnno = ((HandlerMethod) handler).getMethodAnnotation(OmsLoginAccess.class);

        } else {
            return true;
        }
        //登录校验
        checkLogin(loginAnno);
        //运营系统登录校验
        checkOmsLogin(omsLoginAnno);
        //管理员权限校验
        if (checkAdmin(annotation)) return true;

        return true;
    }

    private boolean checkAdmin(UserAccess annotation) {
        if (annotation != null) {
            String value = annotation.value();
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession(false);
            ShiroRealm.ShiroUser member;
            if (session != null) {
                member = (ShiroRealm.ShiroUser) session.getAttribute("member");
                if (member != null) {
                    int workType = member.getWorkType();
                    String identify = member.getIdentify();
                    switch (value) {
                        case "ADMIN":
                            log.debug("需要管理员权限操作");
                            if (!identify.equals("2") && workType == 100) { //企业用户 && 管理员

                                return checkViplevel(annotation, member);

                            } else {
                                log.error("需要管理员权限");
                                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "需要管理员权限");
                            }

                        case "ALL":   //所有用户
                            log.debug("非管理员权限操作");
                            return checkViplevel(annotation, member);

                        default:
                            return true;
                    }
                } else {
                    log.error("请登录");
                    throw new AuthException(MsgCodeConstant.un_login, "请先登录");
                }

            } else {
                log.error("请登录");
                throw new AuthException(MsgCodeConstant.un_login, "请先登录");
            }
        }
        return false;
    }

    /**
     * 验证用户的VIP等级
     *  30:个人黄金 60:个人铂金 130:企业黄金 160:企业铂金
     * @param annotation
     * @param member
     * @return
     */
    private boolean checkViplevel(UserAccess annotation, ShiroRealm.ShiroUser member) {

        String viplevel = annotation.viplevel();
        String[] viplevels = viplevel.split(",");
        List<String> vipList = Arrays.asList(viplevels);

        if (vipList.isEmpty()) {   //无需vip权限
            return true;
        } else { //需要不同的vip权限
            int vipLevel = member.getVipLevel();
            if (!vipList.contains(String.valueOf(vipLevel))) {
                log.error("用户无权限");
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "用户无权限");
            } else {
                return true;
            }
        }

    }

    /**
     * 运营系统用户登录验证
     *
     * @param omsLoginAnno
     */
    private void checkOmsLogin(OmsLoginAccess omsLoginAnno) {
        if (omsLoginAnno != null) {
            boolean require = omsLoginAnno.required();
            if (require) {
                Subject subject = SecurityUtils.getSubject();
                Session session = subject.getSession(false);
                if (session != null) {
                    OMSRealm.ShiroOmsUser member = (OMSRealm.ShiroOmsUser) session.getAttribute("oms");
                    if (member == null) {
                        throw new AuthException(MsgCodeConstant.un_login, "请先登录");
                    }
                } else {
                    throw new AuthException(MsgCodeConstant.un_login, "请先登录");
                }
            }
        }
    }


    /**
     * 用户登录验证
     *
     * @param loginAnno
     */
    private void checkLogin(LoginAccess loginAnno) {
        if (loginAnno != null) {
            boolean require = loginAnno.required();
            if (require) {
                Subject subject = SecurityUtils.getSubject();
                Session session = subject.getSession(false);
                if (session != null) {
                    ShiroRealm.ShiroUser member = (ShiroRealm.ShiroUser) session.getAttribute("member");
                    if (member == null) {
                        throw new AuthException(MsgCodeConstant.un_login, "请先登录");
                    }
                } else {
                    throw new AuthException(MsgCodeConstant.un_login, "请先登录");
                }
            }
        }
    }
}
