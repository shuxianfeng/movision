package com.movision.shiro.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author zhuangyuhao@20160606
 */
public class OrPermissionsAuthorizationFilter extends AuthorizationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException  {
        Subject subject = getSubject(request, response);
        // 获取所有权限
        String[] perms = (String[]) mappedValue;

        for (String perm : perms) {
            if (subject.isPermitted(perm)) {
                return true;
            }
        }

        return false;
    }

}