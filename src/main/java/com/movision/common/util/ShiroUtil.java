package com.movision.common.util;

import com.movision.common.constant.MsgCodeConstant;
import com.movision.common.constant.SessionConstant;
import com.movision.exception.AuthException;
import com.movision.mybatis.user.entity.User;
import com.movision.utils.DateUtils;
import com.movision.utils.propertiesLoader.MsgPropertiesLoader;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.movision.shiro.realm.BossRealm;
import com.movision.shiro.realm.ShiroRealm;

/**
 * shiro工具类
 * 用于获取当前登录人的信息，如id，手机号等
 *
 * @Author zhuangyuhao
 * @Date 2017/2/24 10:01
 */
public class ShiroUtil {
    private static Logger log = LoggerFactory.getLogger(ShiroUtil.class);

    /**
     * 获取APP当前登录人信息
     *
     * @return
     */
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
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.un_login)));

        }
        return member;

    }

    /**
     * 获取BOSS当前登录人信息
     *
     * @return
     */
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
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.un_login)));

        }
        return member;

    }

    /**
     * 获取APP当前登录人id
     * @return
     */
    public static int getAppUserID() {
        int createID = 0;
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(false);
            if (session != null) {
                ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute(SessionConstant.APP_USER);
                log.debug("session中的用户信息，ShiroUser：" + principal.toString());
                if (principal != null) {
                    createID = principal.getId();
                    log.debug("session中的用户id:" + createID);
                }
            } else {
                log.warn("不存在session");
            }
        } catch (Exception e) {
            log.error("get seesion user info error!", e);
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return createID;
    }


    /**
     * 获取boss用户登陆ID
     *
     * @return
     */
    public static Integer getBossUserID() {
        Integer createID = null;
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(false);
            if (session != null) {
                BossRealm.ShiroBossUser principal = (BossRealm.ShiroBossUser) session.getAttribute(SessionConstant.BOSS_USER);
                if (principal != null) {
                    createID = principal.getId();
                }
            }
        } catch (Exception e) {
            log.error("从session中获取当前登录人id失败!", e);
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return createID;
    }

    /**
     * 获取boss用户对应的角色id
     * @return
     */
    public static Integer getBossUserRoleId() {
        Integer roleid = null;
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(false);
            if (session != null) {
                BossRealm.ShiroBossUser principal = (BossRealm.ShiroBossUser) session.getAttribute(SessionConstant.BOSS_USER);
                if (principal != null) {
                    roleid = principal.getRole();
                }
            }
        } catch (Exception e) {
            log.error("从session中获取boss用户对应的角色id失败!", e);
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return roleid;
    }

    /**
     * 修改session中的app用户的积分
     * @param point
     */
    public static void updateAppuserPoint(int point) {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (session != null) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute(SessionConstant.APP_USER);
            //此处可以扩张需要的字段
            principal.setPoints(point);
            session.setAttribute(SessionConstant.APP_USER, principal);
        }
    }

    /**
     * 修改session中的app用户的手机号（用于绑定手机号场景）
     *
     * @param phone
     */
    public static void updateAppuserPhone(String phone) {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (session != null) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute(SessionConstant.APP_USER);
            //此处可以扩张需要的字段
            principal.setPhone(phone);
            session.setAttribute(SessionConstant.APP_USER, principal);
        }
    }

    /**
     * 修改session中的积分和手机号（用于绑定手机号场景）
     *
     * @param phone
     * @param point
     */
    public static void updateAppuserPhoneAndPoints(String phone, Integer point) {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (session != null) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute(SessionConstant.APP_USER);
            //此处可以扩张需要的字段
            principal.setPhone(phone);
            principal.setPoints(point);
            session.setAttribute(SessionConstant.APP_USER, principal);
        }
    }

    /**
     * 更新缓存在session中的app用户信息
     *
     * @param user
     */
    public static void updateAppuser(User user) {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (session != null) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute(SessionConstant.APP_USER);

            principal.setStatus(user.getStatus());
            principal.setPhoto(user.getPhoto());
            principal.setNickname(user.getNickname());
            principal.setLevel(user.getLevel());
            principal.setPhone(user.getPhone());
            principal.setToken(user.getToken());
            principal.setPoints(user.getPoints());
            principal.setSex(user.getSex());
            principal.setSign(user.getSign());
            principal.setBirthday(DateUtils.date2Str(user.getBirthday()));
            principal.setQq(user.getQq());
            principal.setSina(user.getSina());
            principal.setOpenid(user.getOpenid());

            session.setAttribute(SessionConstant.APP_USER, principal);
        }
    }




    /**
     * 修改session中的app用户的第三方账号（用于绑定第三方账号的场景）
     *
     * @param flag
     * @param account
     */
    public static void updateAppuserThirdAccount(Integer flag, String account) {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (session != null) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute(SessionConstant.APP_USER);
            //此处可以扩张需要的字段
            if (flag == 1) {
                principal.setQq(account); //qq
            } else if (flag == 2) {
                principal.setOpenid(account); //微信
            } else {
                principal.setSina(account);   //微博
            }

            session.setAttribute(SessionConstant.APP_USER, principal);
        }
    }


    /**
     * 获取云信id
     *
     * @return
     */
    public static String getAccid() {
        String accid = null;
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(false);
            if (session != null) {
                ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute(SessionConstant.APP_USER);
                if (principal != null) {
                    accid = principal.getAccid();
                }
            }
        } catch (Exception e) {
            log.error("get seesion user info error!", e);
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return accid;
    }

}
