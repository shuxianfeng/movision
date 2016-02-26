package com.zhuhuibao.shiro.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhuhuibao.mybatis.memberReg.entity.Member;
import com.zhuhuibao.mybatis.memberReg.service.MemberRegService;

import java.io.Serializable;


/**
 * @author jianglz
 */
public class ShiroRealm extends AuthorizingRealm {
    private static final Logger log = LoggerFactory.getLogger(ShiroRealm.class);

    @Autowired
    private MemberRegService memberRegService;

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
        log.info("登录认证");
        String loginname = (String) token.getPrincipal();
        //User user = userService.findByMobile(loginname);
        Member member = memberRegService.findMemberByAccount(loginname);
        String account = "";
        if(member != null){
            if(member.getEmail() != null && member.getEmail().indexOf("@") >= 0)
            {
            	account = member.getEmail();
            }
            else
            {
            	account = member.getMobile();
            }
            /*if (!"1".equals(member.getStatus())) {
                throw new LockedAccountException(); // 帐号不正常状态
            }*/
            if ("0".equals(member.getStatus())) {
                throw new LockedAccountException(); // 帐号不正常状态
            }
        }  else{
            throw new UnknownAccountException();//  用户名不存在
        }
        // 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
        /*return new SimpleAuthenticationInfo(
                new ShiroUser(member.getId(), member.getMobile(), member.getEmail(), member.getStatus()), // 用户
                member.getPassword(), // 密码
                ByteSource.Util.bytes("123"),
                getName() // realm name
        );*/
        return new SimpleAuthenticationInfo(
        		account, // 用户
                member.getPassword(), // 密码
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
    public static class ShiroUser implements Serializable {
        private static final long serialVersionUID = -1373760761780840081L;
        private Integer id;
        private String email;
        private String mobile;
        private int status;

        public ShiroUser(Integer id, String name, String mobile, int status) {
            this.id = id;
            this.email = name;
            this.mobile = mobile;
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        /**
         * 重载equals,只计算userid+username;
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

            if (id == null || mobile == null) {
                return false;
            } else if (id.equals(other.id)
                    && mobile.equals(other.mobile))
                return true;
            return false;
        }

    }

}
