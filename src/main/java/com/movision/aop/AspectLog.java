package com.movision.aop;

import com.movision.shiro.realm.ShiroRealm.ShiroUser;
import com.movision.utils.propertiesLoader.PropertiesLoader;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 平台访问日志
 *
 * @author zhuangyuhao@20160413
 */

@Component
@Aspect
public class AspectLog {
    private static final Logger log = LoggerFactory.getLogger(AspectLog.class);

    ThreadLocal<Long> time = new ThreadLocal<Long>();

    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object aroundLog(ProceedingJoinPoint pjp) throws Throwable {
        time.set(System.currentTimeMillis());
        Object result = pjp.proceed();
        Long execTime = System.currentTimeMillis() - time.get();

        long memberId = 0;
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (null != session) {
            ShiroUser principal = (ShiroUser) session.getAttribute("appuser");
            if (null != principal) {
                memberId = principal.getId();
            }
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String clientIP = request.getHeader("X-Real-IP");
        clientIP = clientIP == null ? "127.0.0.1" : clientIP;
        String httpMethod = request.getMethod();
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String userAgent = request.getHeader("User-Agent");

        String logMode = PropertiesLoader.getValue("busi.log.mode");

        switch (logMode) {
            case "db":
                /*AccessLog accessLog = new AccessLog();
                accessLog.setMemberId(memberId);
                accessLog.setClientIP(clientIP);
                accessLog.setHttpMethod(httpMethod);
                accessLog.setRequestURL(requestURL);
                accessLog.setQueryString(queryString);
                accessLog.setUserAgent(userAgent);
                accessLog.setExecTime(execTime);

                int isAdd = accessLogService.addAccessLog(accessLog);*/
                // TODO: 2017/1/16


//                log.debug("zhuangyuhao:AspectLog增加访问日志->isAdd=" + isAdd);
                break;
            case "file":
                log.trace("memberId:[{}]&clientIP:[{}]&httpMethod:[{}]&requestURL:[{}]&queryString[{}]&userAgent[{}]&execTime[{}]",
                        new Object[]{memberId, clientIP, httpMethod, requestURL, queryString, userAgent, execTime});
                break;
            default:
                log.error("业务日志配置不正确");
                break;
        }

        return result;
    }

}