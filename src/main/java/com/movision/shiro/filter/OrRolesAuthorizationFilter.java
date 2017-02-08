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
        //获取当前对象
        Subject subject = getSubject(request, response);

        //获取角色数组
        String[] rolesArray = (String[]) mappedValue;

		if (rolesArray == null || rolesArray.length == 0) {
            //如果没有设置角色参数，默认成功
            return true;
		}

        //判断该请求对象是否有该角色
        Set<String> roles = CollectionUtils.asSet(rolesArray);
        //用户只要满足其中一个角色即可认为是授权认证成功
        for (String role : roles) {
            // 若对象有该角色的权限，则有使用权
            if (subject.hasRole(role)) {
				return true;
			}
		}
		return false;
	}

}