package com.movision.shiro.realm;

import com.movision.common.constant.UserConstants;
import com.movision.facade.user.BossUserFacade;
import com.movision.facade.user.UserRoleRelationFacade;
import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.utils.pagination.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;


/**
 * 验证boss主体的安全数据源
 * @author zhuangyuhao
 */
public class BossRealm extends AuthorizingRealm {
    private static final Logger log = LoggerFactory.getLogger(BossRealm.class);

    @Autowired
    private UserRoleRelationFacade userRoleRelationFacade;

    @Autowired
    private BossUserFacade bossUserFacade;

    /**
     * 授权查询回调函数, 进行鉴权， 当缓存中无用户的授权信息时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        //  获取当前登录对象
        Subject subject = SecurityUtils.getSubject();
        BossUser bossUser = (BossUser) subject.getSession(false).getAttribute("bossuser");
        if (null != bossUser) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            // 判断状态 ，账号状态: 0 正常 1 冻结
            String status = String.valueOf(bossUser.getStatus());
            if (StringUtils.isEmpty(status) || UserConstants.USER_STATUS.disable.toString().equals(status)) {
                return null;
            }

            // 获取用户的角色
            int roleid = userRoleRelationFacade.getRoleidByUserid(bossUser.getId());
            log.debug("当前用户的角色：role=" + roleid);
            // 添加用户角色到授权信息
            info.addRole(String.valueOf(roleid));

            return info;
        }
        return null;
    }

    /**
     * 认证回调函数,登录时调用.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {
        log.info("boss 登录认证");
        String userName = (String) token.getPrincipal();    //用户名
        BossUser bossUser = bossUserFacade.getByUsername(userName);
        if (bossUser != null) {
            log.info("该用户存在");
        }  else{
            throw new UnknownAccountException();    //  用户名不存在
        }

        // 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
        return new SimpleAuthenticationInfo(
                bossUser, // 用户实体
                bossUser.getPassword(), // 密码，这里密码是加密的
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
    /*public static class ShiroOmsUser implements Serializable {
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

        *//**
         * 重载equals,只计算id+account;
     *//*
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

    }*/

}
