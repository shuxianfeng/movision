package com.movision.shiro.realm;

import java.io.Serializable;
import java.util.Arrays;

import com.movision.utils.pagination.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author zhuangyuhao
 */
public class ShiroRealm extends AuthorizingRealm {
    private static final Logger log = LoggerFactory.getLogger(ShiroRealm.class);

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // ShiroUser member = (ShiroUser)
        // principals.fromRealm(getName()).iterator().next();
        Subject subject = SecurityUtils.getSubject();
        ShiroUser member = (ShiroUser) subject.getSession(false).getAttribute("member");
        if (null != member) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

            String status = String.valueOf(member.getStatus());
            String identity = member.getIdentify();
            String role = member.getRole();
            String isexpert = member.getIsexpert();

            if (StringUtils.isEmpty(status) || null == identity || null == role || null == isexpert) {
                return null;
            }

            if (identity.equals("2")) {
                role = "100";
                if (isexpert.equals("1")) {
                    role = "200";
                }
            } else {
                if (identity.length() > 1) {
                    String[] strs = identity.split(",");
                    if (Arrays.asList(strs).contains("3")) {
                        identity = "3,1";
                    } else {
                        identity = "1";
                    }
                } else if (!identity.equals("3")) {
                    identity = "1";
                }

                if (!role.equals("100")) {
                    role = "300";
                }
            }

            String perm = identity + ":" + role + ":" + status;
            info.addRole(status);
            info.addStringPermission(perm);

            return info;
        }
        return null;
    }

    /**
     * 认证回调函数,登录时调用.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("登录认证");
        /*String loginname = (String) token.getPrincipal();
        LoginMember loginMember = memberRegService.getLoginMemberByAccount(loginname);
        if (loginMember != null) {
            if (0 == loginMember.getStatus() || 2 == loginMember.getStatus()) {
                throw new LockedAccountException(); // 帐号不正常状态
            }
        } else {
            throw new UnknownAccountException();// 用户名不存在
        }

        ShiroUser shiroUser = new ShiroUser(loginMember.getId(), loginMember.getAccount(), loginMember.getStatus(), loginMember.getIdentify(), loginMember.getRole(), loginMember.getIsexpert(),
                loginMember.getCompanyId(), loginMember.getRegisterTime(), loginMember.getWorkType(), loginMember.getHeadShot(), loginMember.getNickname(), loginMember.getCompanyName(),
                loginMember.getVipLevel(), loginMember.getEnterpriseLinkman(), loginMember.getFixedTelephone(), loginMember.getEmail());

        // 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
        return new SimpleAuthenticationInfo(shiroUser, // 用户
                loginMember.getPassword(), // 密码
                // ByteSource.Util.bytes("123"),
                getName() // realm name
        );*/
        // TODO: 2017/1/16
        return new SimpleAuthenticationInfo(
                new Object(), // 用户
                new Object(), // 密码
                // ByteSource.Util.bytes("123"),
                getName() // realm name
        );
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
    public void clearAllCachedAuthorizationInfo() {
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache != null) {
            for (Object key : cache.keys()) {
                cache.remove(key);
            }
        }
    }

    /**
     * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
     */
    public static class ShiroUser implements Serializable {
        private static final long serialVersionUID = -1373760761780840081L;
        private Long id;
        private String account;
        private int status;
        private String identify;
        private String role;
        private String isexpert;
        private Long companyId;
        private String companyName;
        private String registerTime;
        private int workType;
        private String headShot;
        private String nickname;

        private int vipLevel;
        /**
         * 联系人
         */
        private String enterpriseLinkman;
        /**
         * 固定电话
         */
        private String fixedTelephone;
        /**
         * 邮箱
         */
        private String email;

        public ShiroUser(Long id, String account, int status, String identify, String role, String isexpert, Long companyId, String registerTime, int workType, String headShot, String nickname,
                String companyName, int vipLevel, String enterpriseLinkman, String fixedTelephone, String email) {

            this.id = id;
            this.account = account;
            this.status = status;
            this.identify = identify;   //身份
            this.role = role;   //
            this.registerTime = registerTime;
            this.nickname = nickname;
            this.vipLevel = vipLevel;
            this.email = email;
        }

        public String getEnterpriseLinkman() {
            return enterpriseLinkman;
        }

        public void setEnterpriseLinkman(String enterpriseLinkman) {
            this.enterpriseLinkman = enterpriseLinkman;
        }

        public String getFixedTelephone() {
            return fixedTelephone;
        }

        public void setFixedTelephone(String fixedTelephone) {
            this.fixedTelephone = fixedTelephone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getIdentify() {
            return identify;
        }

        public void setIdentify(String identify) {
            this.identify = identify;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getIsexpert() {
            return isexpert;
        }

        public void setIsexpert(String isexpert) {
            this.isexpert = isexpert;
        }

        public Long getCompanyId() {
            return companyId;
        }

        public void setCompanyId(Long companyId) {
            this.companyId = companyId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getRegisterTime() {
            return registerTime;
        }

        public void setRegisterTime(String registerTime) {
            this.registerTime = registerTime;
        }

        public int getWorkType() {
            return workType;
        }

        public void setWorkType(int workType) {
            this.workType = workType;
        }

        public String getHeadShot() {
            return headShot;
        }

        public void setHeadShot(String headShot) {
            this.headShot = headShot;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getVipLevel() {
            return vipLevel;
        }

        public void setVipLevel(int vipLevel) {
            this.vipLevel = vipLevel;
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

            if (id == null || account == null) {
                return false;
            } else if (id.equals(other.id) && account.equals(other.account))
                return true;
            return false;
        }


    }

}
