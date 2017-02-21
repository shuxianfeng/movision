package com.movision.common.util;

import com.movision.common.constant.MsgCodeConstant;
import com.movision.common.constant.SessionConstant;
import com.movision.exception.AuthException;
import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.utils.MsgPropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.movision.shiro.realm.BossRealm;
import com.movision.shiro.realm.ShiroRealm;

public class ShiroUtil {
    private static Logger log = LoggerFactory.getLogger(ShiroUtil.class);

    public static ShiroRealm.ShiroUser getAppUser() {
        ShiroRealm.ShiroUser member = null;
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(false);
            if (session != null) {
                member = (ShiroRealm.ShiroUser) session.getAttribute(SessionConstant.APP_USER);
            }
        } catch (Exception e) {
            log.error("从session中获取当前登录的app用户失败!", e);
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));

        }
        return member;

    }

    public static BossRealm.ShiroBossUser getBossUser() {
        BossRealm.ShiroBossUser member = null;
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(false);
            if (session != null) {
                member = (BossRealm.ShiroBossUser) session.getAttribute(SessionConstant.BOSS_USER);
            }
        } catch (Exception e) {
            log.error("从session中获取当前登录的Boss用户失败!", e);
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));

        }
        return member;

    }

    public static int getAppUserID() {
        int createID = 0;
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(false);
            if (session != null) {
                ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute(SessionConstant.APP_USER);
                if (principal != null) {
                    createID = principal.getId();
                }
            }
        } catch (Exception e) {
            log.error("get seesion user info error!", e);
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return createID;
    }


    /**
     * 获取boss用户登陆ID
     *
     * @return
     */
    public static Long getBossUserID() {
        Long createID = null;
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(false);
            if (session != null) {
                BossRealm.ShiroBossUser principal = (BossRealm.ShiroBossUser) session.getAttribute(SessionConstant.BOSS_USER);
                if (principal != null) {
                    createID = Long.valueOf(principal.getId() + "");
                }
            }
        } catch (Exception e) {
            log.error("从session中获取当前登录人id失败!", e);
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return createID;
    }

    /**
     * 实时更新登录者在session中的信息
     * 
     * @param status
     * @param role
     */
    public static void updateShiroUser(int status, String role, String headShot) {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (session != null) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute(SessionConstant.APP_USER);
            principal.setStatus(status);
            principal.setRole(role);
            if (StringUtils.isNotBlank(headShot)) {
                principal.setPhoto(headShot);
            }
            session.setAttribute("appuser", principal);
        }
    }
}
