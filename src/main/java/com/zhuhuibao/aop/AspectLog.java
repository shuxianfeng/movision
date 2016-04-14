package com.zhuhuibao.aop;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import com.zhuhuibao.shiro.realm.ShiroRealm.ShiroUser;
import com.zhuhuibao.mybatis.accessLog.entity.AccessLog;
import com.zhuhuibao.mybatis.accessLog.service.AccessLogService;

/**
 * 平台访问日志
 * @author caijl@20160413
 */

public class AspectLog {
	private static final Logger log = LoggerFactory
			.getLogger(AspectLog.class);
	
	@Autowired
	private AccessLogService accessLogService;
	
	ThreadLocal<Long> time = new ThreadLocal<Long>();

	public Object aroundLog(ProceedingJoinPoint pjp) throws Throwable{
		time.set(System.currentTimeMillis());
		Object result = pjp.proceed();
		Long execTime =  System.currentTimeMillis()-time.get();
		
        long memberId = 0;
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null != session){
        	ShiroUser principal = (ShiroUser)session.getAttribute("member");
        	if(null != principal){
        		memberId = principal.getId();
        	}
        }
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String clientIP = request.getRemoteAddr();
        String httpMethod = request.getMethod();
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String userAgent = request.getHeader("User-Agent");
        
        AccessLog accessLog = new AccessLog();
        accessLog.setMemberId(memberId);
        accessLog.setClientIP(clientIP);
        accessLog.setHttpMethod(httpMethod);
        accessLog.setRequestURL(requestURL);
        accessLog.setQueryString(queryString);
        accessLog.setUserAgent(userAgent);
        accessLog.setExecTime(execTime);
        int isAdd = accessLogService.addAccessLog(accessLog);
        
        log.debug("caijl:AspectLog增加访问日志->isAdd="+isAdd);
		
		return result;
	}

}