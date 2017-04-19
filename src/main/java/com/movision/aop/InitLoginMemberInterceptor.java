package com.movision.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.movision.common.constant.ImConstant;
import com.movision.common.constant.SessionConstant;
import com.movision.facade.boss.BossLoginFacade;
import com.movision.facade.im.ImFacade;
import com.movision.facade.user.BossUserFacade;
import com.movision.facade.user.UserFacade;
import com.movision.facade.user.UserRoleRelationFacade;
import com.movision.mybatis.bossMenu.entity.AuthMenu;
import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.mybatis.imuser.entity.ImUser;
import com.movision.mybatis.user.entity.LoginUser;
import com.movision.shiro.realm.BossRealm;
import com.movision.utils.DateUtils;
import com.movision.utils.propertiesLoader.LoginPropertiesLoader;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.movision.shiro.realm.ShiroRealm;
import com.movision.shiro.realm.ShiroRealm.ShiroUser;

import java.util.List;
import java.util.Map;

/**
 * 请求拦截器，初始化登录用户信息
 * PS: app端的请求都不走这个拦截器,只有Boss端的请求会走
 *
 * 系统处理顺序：
 * shiroFilter ——> shiroInterceptor ——> InitLoginMemberInterceptor ——> Controller
 *
 * @Author zhuangyuhao
 * @Date 2017/2/20 10:16
 */
public class InitLoginMemberInterceptor extends HandlerInterceptorAdapter {
    private Logger log = LoggerFactory.getLogger(InitLoginMemberInterceptor.class);

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private ShiroRealm shiroRealm;

    @Autowired
    private BossRealm bossRealm;

    @Autowired
    private BossUserFacade bossUserFacade;

    @Autowired
    private UserRoleRelationFacade userRoleRelationFacade;

    @Autowired
    private BossLoginFacade bossLoginFacade;

    @Autowired
    private ImFacade imFacade;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getServletPath(); //  path=/boss/menu/query_sidebar
        /**
         *  如果是/boss/menu/menu_list.html应该怎么拦截？
         *      如果他的角色拥有 /boss/menu的权限，那么就不拦截
         *      如果没有 /boss/menu的权限，就拦截
         */
        log.info("path=" + path);
        /**
         * 请求中带这些字符串的不去拦截
         * app端的请求都不走这个拦截器
         */
        if (path.matches(LoginPropertiesLoader.getValue("no.intercept.url"))) {
            return true;
        } else {
            // shiro管理的session
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(false);
            BossRealm.ShiroBossUser bossUser = (BossRealm.ShiroBossUser) session.getAttribute(SessionConstant.BOSS_USER);
            ShiroUser appuser = (ShiroUser) session.getAttribute(SessionConstant.APP_USER);
            if (bossUser != null && appuser == null) {
                // 判断是否拥有当前点击菜单的权限（内部过滤,防止通过url进入跳过菜单权限）
                /**
                 * 根据点击的菜单中的URL去匹配，当匹配到了此菜单，判断是否有此菜单的权限，没有的话跳转到404页面
                 */
                int role = bossUser.getRole();
                if (role == 1) {
                    //超管拥有所有权限
                    return true;
                }

                //获取授权的菜单列表
                List<Map<String, Object>> menuList = (List) session.getAttribute(SessionConstant.ACCESS_MENU);
                log.info("获取授权的菜单列表：" + menuList.toString());
                //遍历菜单列表
                for (int i = 0; i < menuList.size(); i++) {
                    //获取一个map中的父菜单
                    AuthMenu parentMenu = (AuthMenu) menuList.get(i).get("parent_menu");
                    //父菜单的url
                    String parentUrl = parentMenu.getUrl();
                    /*if (path.contains(parentUrl) && parentMenu.getAuthroize()) {
                        this.initBossUserInfo(currentUser, session);
                        return true;
                    }*/
                    //获取一个map钟的子菜单集合
                    List<AuthMenu> childrenList = (List<AuthMenu>) menuList.get(i).get("child_menu");
                    //遍历子菜单
                    for (int j = 0; j < childrenList.size(); j++) {
                        //子菜单的url
                        String childUrl = childrenList.get(j).getUrl();
                        //
                        /**
                         * 若请求的url包含父菜单的url，并且 子菜单url包含父菜单url， 并且 该菜单属于授权菜单，
                         * 则通过拦截器
                         */
                        if (path.contains(parentUrl) && childUrl.contains(parentUrl) && childrenList.get(j).getAuthroize()) {
                            this.initBossUserInfo(currentUser, session);
                            return true;
                        }
                    }
                }
                //需要返回：无权限访问该请求
//                response.sendRedirect(request.getContextPath() + "rest/exception/error_401");
                bossLoginFacade.handleNoPermission(response);
                return false;
            } else if (bossUser == null && appuser != null) {
                //app端不做菜单控制, 所以该分支永远不会进入
                this.initAppUserInfo(currentUser, session);
                return true;

            } else {

                log.error("请登录");
                bossLoginFacade.handleNotLogin(response);
                return false;
                //跳转到登录界面
//                String str=SmartConfig.getString("smart.login");
//                response.sendRedirect(request.getContextPath() + "app/login");
//                log.info("sendRedirect的路径："+request.getContextPath() + "app/login");
//                throw new AuthException(MsgCodeConstant.un_login, "请先登录");

            }
        }
    }


    /**
     * 初始化boss用户信息
     *
     * @param subject
     * @param session
     */
    private void initBossUserInfo(Subject subject, Session session) {
        //获取当前会话登录人：boss用户
        BossRealm.ShiroBossUser bossuser = (BossRealm.ShiroBossUser) session.getAttribute(SessionConstant.BOSS_USER);
        if (null != bossuser) {
            log.info("从缓存中获取boss用户成功，shiroBossUser=" + bossuser.toString());
            //根据用户名获取用户信息
            BossUser bossUser = bossUserFacade.getUserByPhone(bossuser.getPhone());
            int roleid = userRoleRelationFacade.getRoleidByUserid(bossUser.getId());
            //获取imuser
            ImUser imUser = imFacade.getImuser(bossUser.getId(), ImConstant.TYPE_BOSS);
            String accid = null == imUser ? null : imUser.getAccid();
            String imtoken = null == imUser ? null : imUser.getToken();


            if (null != bossUser) {
                // 初始化登录信息
                BossRealm.ShiroBossUser loginInfo = new BossRealm.ShiroBossUser(bossUser.getId(), bossUser.getName(),
                        bossUser.getPhone(), bossUser.getUsername(),
                        bossUser.getPassword(), bossUser.getIssuper(), bossUser.getStatus(), bossUser.getIsdel(), bossUser.getCreatetime(),
                        bossUser.getAfterlogintime(), bossUser.getBeforelogintime(), roleid, accid, imtoken,
                        bossUser.getIscircle(), bossUser.getCirclemanagement(), bossUser.getContributing(), bossUser.getCommon());

                //判断登录信息是否改变,若改变了则更新session，
                if (this.loginBossInfoIsChange(bossuser, loginInfo)) {
                    session.setAttribute(SessionConstant.BOSS_USER, loginInfo);
                    //清除缓存
                    bossRealm.getAuthorizationCache().remove(String.valueOf(subject.getPrincipals()));
                }
            }
        }
    }

    /**
     * 初始化app用户信息
     *
     * @param subject
     * @param session
     */
    private void initAppUserInfo(Subject subject, Session session) {
        // 获取当前会话登录人：app用户
        ShiroUser appuser = (ShiroUser) session.getAttribute(SessionConstant.APP_USER);
        if (null != appuser) {
            log.info("从缓存中获取app用户成功，appuser=" + appuser.toString());
            //根据用户名获取用户信息
            LoginUser loginUser = userFacade.getLoginUserByPhone(appuser.getPhone());

            if (null != loginUser) {
                // 初始化登录信息
                ShiroUser loginInfo = new ShiroUser(loginUser.getId(), loginUser.getPhone(), loginUser.getStatus(), loginUser.getRole(),
                        loginUser.getIntime(), loginUser.getPhoto(), loginUser.getNickname(), loginUser.getLevel(),
                        loginUser.getPhone(), loginUser.getToken(), loginUser.getPoints(), loginUser.getSex(),
                        loginUser.getAccid(), loginUser.getImtoken(), loginUser.getSign(), DateUtils.date2Str(loginUser.getBirthday()),
                        loginUser.getQq(), loginUser.getSina(), loginUser.getOpenid());
                //判断登录信息是否改变,若改变了则更新session，

                //判断登录信息是否改变,若改变了则更新session，
                if (this.loginUserInfoIsChange(appuser, loginInfo)) {
                    session.setAttribute(SessionConstant.APP_USER, loginInfo);
                    shiroRealm.getAuthorizationCache().remove(String.valueOf(subject.getPrincipals()));
                }
            }
        }
    }

    /**
     * 判断appuser登录信息是否改变
     *
     * @param appuser
     * @param loginInfo
     * @return
     */
    private boolean loginUserInfoIsChange(ShiroUser appuser, ShiroUser loginInfo) {
        boolean isChange = appuser == null || loginInfo == null || loginInfo.getLevel() != appuser.getLevel()
                || loginInfo.getStatus() != appuser.getStatus() || !loginInfo.getRole().equals(appuser.getRole())
                || loginInfo.getToken() != appuser.getToken() || loginInfo.getAccid() != appuser.getAccid()
                || loginInfo.getImToken() != appuser.getImToken()
                || loginInfo.getSign() != appuser.getSign()
                || loginInfo.getBirthday() != appuser.getBirthday()
                || loginInfo.getQq() != appuser.getQq()
                || loginInfo.getSina() != appuser.getSina()
                || loginInfo.getOpenid() != appuser.getOpenid();
        return isChange;
    }

    /**
     * 判断bossuser登录信息是否改变
     *
     * @param bossuser
     * @param loginInfo
     * @return
     */
    private boolean loginBossInfoIsChange(BossRealm.ShiroBossUser bossuser, BossRealm.ShiroBossUser loginInfo) {
        boolean isChange = bossuser == null || loginInfo == null || bossuser.getRole() != loginInfo.getRole()
                || bossuser.getPhone() != loginInfo.getPhone() || bossuser.getPassword() != loginInfo.getPassword()
                || bossuser.getUsername() != loginInfo.getUsername()
                || bossuser.getAccid() != loginInfo.getAccid()
                || bossuser.getImtoken() != loginInfo.getImtoken()
                || bossuser.getCirclemanagement() != loginInfo.getCirclemanagement()
                || bossuser.getCommon() != loginInfo.getCommon()
                || bossuser.getContributing() != loginInfo.getContributing()
                || bossuser.getIscircle() != loginInfo.getIscircle();
        return isChange;
    }
}
