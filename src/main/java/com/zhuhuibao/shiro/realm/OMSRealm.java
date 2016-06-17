package com.zhuhuibao.shiro.realm;

import com.zhuhuibao.mybatis.oms.entity.User;
import com.zhuhuibao.mybatis.oms.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;


/**
 * @author jianglz
 */
public class OMSRealm extends AuthorizingRealm {
    private static final Logger log = LoggerFactory.getLogger(OMSRealm.class);

    @Autowired
    private UserService userService;

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        return null;
    }

    /**
     * 认证回调函数,登录时调用.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {
        log.info("oms 登录认证");
        String userName = (String) token.getPrincipal();
        User user = userService.selectUserByAccount(userName);
        if(user != null){

        }  else{
            throw new UnknownAccountException();//  用户名不存在
        }
        
        // 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
        return new SimpleAuthenticationInfo(
                new ShiroOmsUser(user.getId(), userName), // 用户
                user.getPassword(), // 密码
//                ByteSource.Util.bytes("123"),
                getName() // realm name
        );
    }


    /**
     * 更新用户授权信息缓存.
     */
    public void clearCachedAuthorizationInfo(Object principal) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(
                principal, getName());
        clearCachedAuthorizationInfo(principals);
    }


    /**
     * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
     */
    public static class ShiroOmsUser implements Serializable {
        private static final long serialVersionUID = -1373760761780840081L;
        private Integer id;
        private String account;

        public ShiroOmsUser(Integer id, String account) {
            this.id = id;
            this.account = account;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

		public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
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
            ShiroOmsUser other = (ShiroOmsUser) obj;

            if (id == null || account == null) {
                return false;
            } else if (id.equals(other.id)
                    && account.equals(other.account))
                return true;
            return false;
        }

    }

}
