package com.zhuhuibao.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

/**
 * @author caijl@20160606
 */
public class OrPermissionsAuthorizationFilter extends AuthorizationFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException  {
		Subject subject = getSubject(request, response);
		String[] perms = (String[]) mappedValue;

		for (String perm : perms) {
			if (subject.isPermitted(perm)) {
				return true;
			}
		}
		
		return false;
	}

}