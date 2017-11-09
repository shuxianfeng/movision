package com.movision.common.util;

import com.movision.common.constant.MsgCodeConstant;
import com.movision.common.constant.SessionConstant;
import com.movision.exception.AuthException;
import com.movision.mybatis.user.entity.LoginUser;
import com.movision.shiro.realm.BossRealm;
import com.movision.shiro.realm.ShiroRealm;
import com.movision.utils.DateUtils;
import com.movision.utils.propertiesLoader.MsgPropertiesLoader;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * 修改session中的bossuser的pwd
     *
     * @param pwd
     */
    public static void updateBossuserPwd(String pwd) {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (session != null) {
            BossRealm.ShiroBossUser principal = (BossRealm.ShiroBossUser) session.getAttribute(SessionConstant.BOSS_USER);
            if (null != principal) {
                //此处可以扩张需要的字段
                principal.setPassword(pwd);
                session.setAttribute(SessionConstant.BOSS_USER, principal);
            }

        }
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
            if (null != principal) {
                //此处可以扩展需要的字段
                principal.setPoints(point);
                session.setAttribute(SessionConstant.APP_USER, principal);
            }
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
            if (null != principal) {
                //此处可以扩张需要的字段
                principal.setPhone(phone);
                session.setAttribute(SessionConstant.APP_USER, principal);
            }

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
            if (null != principal) {
                //此处可以扩张需要的字段
                principal.setPhone(phone);
                principal.setPoints(point);
                principal.setAccount(phone);
                session.setAttribute(SessionConstant.APP_USER, principal);
            }
        }
    }

    /**
     * 更新缓存在session中的app用户信息
     *
     * @param loginUser
     */
    public static void updateAppuser(LoginUser loginUser) {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (session != null) {
            session.setAttribute(SessionConstant.APP_USER, getShiroUserFromLoginUser(loginUser));
        }
    }

    /**
     * 从 LoginUser 转换为 ShiroUser
     *
     * @param loginUser
     * @return
     */
    public static ShiroRealm.ShiroUser getShiroUserFromLoginUser(LoginUser loginUser) {
        return new ShiroRealm.ShiroUser(loginUser.getId(), loginUser.getPhone(), loginUser.getStatus(), loginUser.getRole(),
                loginUser.getIntime(), loginUser.getPhoto(), loginUser.getNickname(), loginUser.getLevel(), loginUser.getPhone(),
                loginUser.getToken(), loginUser.getPoints(), loginUser.getSex(), loginUser.getAccid(), loginUser.getImtoken(),
                loginUser.getSign(), DateUtils.date2Str(loginUser.getBirthday()),
                loginUser.getQq(), loginUser.getSina(), loginUser.getOpenid(), loginUser.getHeatValue(), loginUser.getIpCity(),
                loginUser.getLatitude(), loginUser.getLongitude()
        );
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
            if (null != principal) {
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

    public static String getIpCity() {
        String ipCity = null;
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(false);
            if (session != null) {
                ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute(SessionConstant.APP_USER);
                if (principal != null) {
                    ipCity = principal.getIpCity();
                }
            }
        } catch (Exception e) {
            log.error("get seesion user info error!", e);
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return ipCity;
    }

    /**
     * 获取app用户等级，0-99， 默认是0
     *
     * @return
     */
    public static Integer getUserLevel() {
        Integer level = 0;  //默认是0
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(false);
            if (session != null) {
                ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute(SessionConstant.APP_USER);
                if (principal != null) {
                    level = principal.getLevel();
                }
            }
        } catch (Exception e) {
            log.error("get seesion user info error!", e);
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return level;
    }

    public static String getLongitude() {
        String longitude = null;
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(false);
            if (session != null) {
                ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute(SessionConstant.APP_USER);
                if (principal != null) {
                    longitude = principal.getLongitude();
                }
            }
        } catch (Exception e) {
            log.error("get seesion user info error!", e);
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return longitude;
    }

    public static String getLatitude() {
        String latitude = null;
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(false);
            if (session != null) {
                ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute(SessionConstant.APP_USER);
                if (principal != null) {
                    latitude = principal.getLatitude();
                }
            }
        } catch (Exception e) {
            log.error("get seesion user info error!", e);
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return latitude;
    }

}
