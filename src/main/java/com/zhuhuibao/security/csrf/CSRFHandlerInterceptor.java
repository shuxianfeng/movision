package com.zhuhuibao.security.csrf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;

public class CSRFHandlerInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof DefaultServletHttpRequestHandler) {
            return true;
        }
        if (!request.getMethod().equalsIgnoreCase("POST")) {
            // Not a POST - allow the request
            return true;
        } else {
            // This is a POST request - need to check the CSRF token
            String sessionToken = CSRFTokenManager.getTokenForSession(request
                    .getSession());
            String requestToken = CSRFTokenManager.getTokenFromRequest(request);
            if (sessionToken.equals(requestToken)) {
                return true;
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Bad or missing CSRF value");
                return false;
            }
        }
    }

}
