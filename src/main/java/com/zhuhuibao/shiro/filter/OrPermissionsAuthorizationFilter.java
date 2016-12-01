package com.zhuhuibao.shiro.filter;

import com.zhuhuibao.shiro.realm.ShiroRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author caijl@20160606
 */
public class OrPermissionsAuthorizationFilter extends AuthorizationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        Subject subject = getSubject(request, response);
        String[] perms = (String[]) mappedValue;

        if (null == subject.getPrincipal()) {
            return false;
        }

        for (String perm : perms) {
            String[] permArr = perm.split(":");
            if (!isAccessAllowedRealization(permArr, subject)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 权限过滤实现
     *
     * @return
     */
    private boolean isAccessAllowedRealization(String[] permArr, Subject subject) {
        if (permArr.length > 0) {
            ShiroRealm.ShiroUser user = ((ShiroRealm.ShiroUser) subject.getPrincipals().getPrimaryPrincipal());
            String key = permArr[0];
            // 角色判断
            if (key.equals("role")) {
                for (int i = 1; i < permArr.length; i++) {
                    if (user.isEnterprise()) {

                    }
                }
            } else if (key.equals("wokeType")) {

            }


        }
        return true;
    }

}