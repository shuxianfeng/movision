package com.movision.shiro.filter;

import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.shiro.realm.ShiroRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static org.apache.shiro.web.filter.mgt.DefaultFilter.user;

/**
 * shiro权限过滤器【废弃】
 * @author zhuangyuhao@20160606
 */
public class OrPermissionsAuthorizationFilter extends AuthorizationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException  {
        Subject subject = getSubject(request, response);
        // 获取所有权限
        String[] perms = (String[]) mappedValue;

        for (String perm : perms) {
            String[] permArr = perm.split(":");
            if (!isAccessAllowedRealization(permArr, subject)) {
                return false;
            }
        }

        return false;
    }

    /**
     * 权限过滤实现
     *
     * @return
     */
    private boolean isAccessAllowedRealization(String[] permArr, Subject subject) {
        if (permArr.length > 1) {
            String key = permArr[0];
            //判断当前的首要身份
            if (subject.getPrincipals().getPrimaryPrincipal() instanceof BossUser) {
                if (key.equals("role")) {
                    // 账号角色 1 boss管理员 2 boss普通用户
                    if ("1".equals(permArr[1])) {
                        return false;
                    } else if ("2".equals(permArr[1])) {
                        return false;
                    }
                }
            }

            if (subject.getPrincipals().getPrimaryPrincipal() instanceof ShiroRealm.ShiroUser) {
                ShiroRealm.ShiroUser user = ((ShiroRealm.ShiroUser) subject.getPrincipals().getPrimaryPrincipal());
                if (key.equals("role")) {
                    // 账号角色 3 app管理员 4 app普通用户
                    boolean isAdmin = user.getRole().equals("3");
                    if ("3".equals(permArr[1]) && !isAdmin) {
                        return false;
                    } else if ("4".equals(permArr[1]) && isAdmin) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}