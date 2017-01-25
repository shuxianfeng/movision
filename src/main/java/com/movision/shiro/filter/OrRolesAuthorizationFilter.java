package com.movision.shiro.filter;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

/**
 * 角色授权过滤器
 * @author zhuangyuhao@20160606
 */
public class OrRolesAuthorizationFilter extends AuthorizationFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {

		Subject subject = getSubject(request, response);
        //获取角色数组
        String[] rolesArray = (String[]) mappedValue;

		if (rolesArray == null || rolesArray.length == 0) {
			// no roles specified, so nothing to check - allow access.
            // 没有定义角色，那么就不需要检查访问权限
            return true;
		}

        //判断该请求对象是否有该角色
        Set<String> roles = CollectionUtils.asSet(rolesArray);
		for (String role : roles) {
            // 若对象有该角色的权限，则有使用权
            if (subject.hasRole(role)) {
				return true;
			}
		}
		return false;
	}

}