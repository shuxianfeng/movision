package com.zhuhuibao.security.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jianglz
 * @since 16/7/6.
 */

@ComponentScan
public class RefererFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(RefererFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        //获取HTTP HEAD Referer
        String referer = req.getHeader("referer");
        log.debug("http header referer >>>>>" + referer);
        String serverName = req.getServerName();
        log.debug("serverName >>> " + serverName);
        if(referer == null ||  !referer.contains(serverName)){
             resp.sendError(403);
        } else{
            chain.doFilter(req,resp);
        }

    }

    @Override
    public void destroy() {

    }
}
