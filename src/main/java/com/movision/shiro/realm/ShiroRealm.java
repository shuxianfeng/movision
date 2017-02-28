package com.movision.shiro.realm;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.Gson;
import com.movision.common.constant.SessionConstant;
import com.movision.common.constant.UserConstants;
import com.movision.facade.user.BossUserFacade;
import com.movision.facade.user.UserFacade;
import com.movision.mybatis.user.entity.LoginUser;
import com.movision.utils.pagination.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * App用户安全数据源
 * @author zhuangyuhao
 */
public class ShiroRealm extends AuthorizingRealm {
    private static final Logger log = LoggerFactory.getLogger(ShiroRealm.class);

    @Autowired
    private UserFacade userFacade;

    /**
     * 表示获取身份验证信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("APP登录认证");
        String loginPhone = (String) token.getPrincipal();
        // 1 获取当前登录的用户信息
        LoginUser loginUser = userFacade.getLoginUserByPhone(loginPhone);
        log.debug("当前登录APP的用户信息，LoginUser = " + loginUser.toString());

        if (loginUser != null) {
            if (1 == loginUser.getStatus()) {
                throw new LockedAccountException(); // 帐号异常封号
            }
        } else {
            throw new UnknownAccountException();// 用户手机号不存在
        }

        //登录的pwd
        String loginPwd = String.valueOf(((UsernamePasswordToken) token).getPassword());
        log.debug("登录的pwd，" + loginPwd);

        //获取服务端的密码，并MD5二次加密
        String mytokenStr = loginUser.getToken();
        Gson gson = new Gson();
        UsernamePasswordToken mytoken = gson.fromJson(mytokenStr, UsernamePasswordToken.class);
        String password = String.valueOf(mytoken.getPassword());
        String pwd = new Md5Hash(password, null, 2).toString();
        log.debug("服务端的pwd," + password);

        // 2 根据登录用户信息生成ShiroUser用户
        ShiroUser shiroUser = new ShiroUser(loginUser.getId(), loginUser.getPhone(), loginUser.getStatus(), loginUser.getRole(),
                loginUser.getIntime(), loginUser.getPhoto(), loginUser.getNickname(), loginUser.getLevel(), loginUser.getPhone(),
                loginUser.getToken());

        // 3 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
        return new SimpleAuthenticationInfo(shiroUser, // 用户
                pwd,
                getName() // realm name
        );

    }

    /**
     * 表示根据用户身份获取授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("清除App用户授权信息缓存");
        this.clearCachedAuthorizationInfo(principals);
        //  获取当前登录对象
        Subject subject = SecurityUtils.getSubject();
        ShiroUser member = (ShiroUser) subject.getSession(false).getAttribute(SessionConstant.APP_USER);
        if (null != member) {

            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

            String status = String.valueOf(member.getStatus());
            if (StringUtils.isEmpty(status) || UserConstants.USER_STATUS.disable.toString().equals(status)) {
                return null;
            }
            //获取用户的角色
            LoginUser loginUser = userFacade.getLoginUserByPhone(member.getPhone());
            String role = loginUser.getRole();
            log.debug("当前登录对象的角色,role=" + role);
            info.addRole(role);

            return info;
        }
        return null;
    }


    /**
     * 更新用户授权信息缓存.
     */
    public void clearCachedAuthorizationInfo(Object principal) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
        clearCachedAuthorizationInfo(principals);
    }

    // 登陆成功后强制加载shiro权限缓存 避免懒加载 先清除
    public void forceShiroToReloadUserAuthorityCache() {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipal());
        // this.isPermitted(SecurityUtils.getSubject().getPrincipals(),
        // "强制加载缓存，避免懒加载" + System.currentTimeMillis());
    }

    /**
     * 清除所有用户授权信息缓存.
     */
    /*public void clearAllCachedAuthorizationInfo() {
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache != null) {
            for (Object key : cache.keys()) {
                cache.remove(key);
            }
        }
    }*/

    /**
     * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
     */
    public static class ShiroUser implements Serializable {
        private static final long serialVersionUID = -1373760761780840081L;
        private Integer id;
        private String account; //账号(使用手机号)
        private Integer status; //账号状态：默认 0 正常  1 异常封号
        private String role;       //角色
        private Date registerTime;    //注册时间
        private String photo;    //头像url
        private String nickname;    //昵称
        private Integer level;   //用户等级：0 普通用户  1 青铜  2 白银 3 黄金 4 白金 5 钻石 6 金钻石 7皇冠 8金皇冠
        private String phone;   //手机号
        private String token;

        public void setId(Integer id) {
            this.id = id;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public void setRegisterTime(Date registerTime) {
            this.registerTime = registerTime;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Integer getId() {
            return id;
        }

        public String getAccount() {
            return account;
        }

        public Integer getStatus() {
            return status;
        }

        public String getRole() {
            return role;
        }

        public Date getRegisterTime() {
            return registerTime;
        }

        public String getPhoto() {
            return photo;
        }

        public String getNickname() {
            return nickname;
        }

        public Integer getLevel() {
            return level;
        }

        public String getPhone() {
            return phone;
        }

        public String getToken() {
            return token;
        }

        public ShiroUser(Integer id, String account, Integer status, String role, Date registerTime, String photo, String nickname, Integer level, String phone, String token) {
            this.id = id;
            this.account = account;
            this.status = status;
            this.role = role;
            this.registerTime = registerTime;
            this.photo = photo;
            this.nickname = nickname;
            this.level = level;
            this.phone = phone;
            this.token = token;
        }

        /**
         * 重载equals,只计算id+account;
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ShiroUser other = (ShiroUser) obj;

            if (id == 0 || account == null) {
                return false;
            } else if (id == other.id && account.equals(other.account))
                return true;
            return false;
        }

        @Override
        public String toString() {
            return "ShiroUser{" +
                    "id=" + id +
                    ", account='" + account + '\'' +
                    ", status=" + status +
                    ", role='" + role + '\'' +
                    ", registerTime=" + registerTime +
                    ", photo='" + photo + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", level=" + level +
                    ", phone='" + phone + '\'' +
                    ", token='" + token + '\'' +
                    '}';
        }
    }

}
