package com.zhuhuibao.security.resubmit;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jianglz
 * @version 2016-03-03
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(TokenInterceptor.class);
    private static Map<String , String> viewUrls = new HashMap<String , String>();
    private static Map<String , String> actionUrls = new HashMap<String , String>();
    private Object clock = new Object();

//    static
//    {
//        viewUrls.put("/rest/mobileCode", "GET");
//
//        actionUrls.put("/rest/register", "POST");
//
//    }

    /**
     * 拦截方法，添加or验证token
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String url = request.getRequestURI();
        String method = request.getMethod();
//        if(viewUrls.keySet().contains(url) && ((viewUrls.get(url)) == null || viewUrls.get(url).equals(method))) {
////            TokenHelper.setToken(request);
//            return true;
//        }
//        else if(actionUrls.keySet().contains(url) && ((actionUrls.get(url)) == null || actionUrls.get(url).equals(method))) {
//            log.debug("Intercepting invocation to check for valid transaction token.");
//            return handleToken(request, response, handler);
//        }
        return !method.equals("POST") || handleToken(request, response, handler);
    }

    protected boolean handleToken(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        synchronized(clock)
        {
            if(!TokenHelper.validToken(request))
            {
                System.out.println("未通过验证...");
                return handleInvalidToken(request, response, handler);
            }
        }
        System.out.println("通过验证...");
        return handleValidToken(request, response, handler);
    }

    /**
     * 当出现一个非法令牌时调用
     */
    protected boolean handleInvalidToken(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        JsonResult result = new JsonResult();
        result.setCode(400);
        result.setMessage("请不要频繁操作");
        writeMessageUtf8(response, result);
        return false;
    }

    /**
     * 当发现一个合法令牌时调用.
     */
    protected boolean handleValidToken(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        return true;
    }

    private void writeMessageUtf8(HttpServletResponse response, JsonResult  json) throws IOException {
        try
        {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(JsonUtils.getJsonStringFromObj(json));
        }
        finally
        {
            response.getWriter().close();
        }
    }

}
