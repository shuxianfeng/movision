package com.zhuhuibao.security.resubmit;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author jianglz
 * @version 2016-03-03
 */
public class TokenHelper {
    private static final Logger log = LoggerFactory.getLogger(TokenHelper.class);

    /**
     * 保存token值的默认命名空间
     */
    public static final String TOKEN_NAMESPACE = "zhb.tokens";

    /**
     * 持有token名称的字段名
     */
    public static final String TOKEN_NAME_FIELD = "resToken";

    private static final Random RANDOM = new Random();


    /**
     * 使用随机字串作为token名字保存token
     *
     * @param request
     * @return token
     */
    public static String setToken(HttpServletRequest request) {
        return setToken(request, generateGUID());
    }

    /**
     * 使用随机字串作为token名字保存token
     */
    public static String setToken(HttpServletRequest req, String tokenName) {
        String token = generateGUID();
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(true);
//            HttpSession session = req.getSession(false);
            session.setAttribute(TOKEN_NAME_FIELD, tokenName);
            session.setAttribute(tokenName, token);

        } catch (IllegalStateException e) {
            String msg = "Error creating HttpSession due response is commited to client. You can use the CreateSessionInterceptor or create the HttpSession from your action before the result is rendered to the client: " + e.getMessage();
            log.error(msg, e);
            throw new IllegalArgumentException(msg);
        }
        return token;
    }

    /**
     * 从请求域中获取给定token名字的token值
     *
     * @param tokenName
     * @return the token String or null, if the token could not be found
     */
    public static String getToken(HttpServletRequest request, String tokenName) {
        if (tokenName == null) {
            return null;
        }
        Map params = request.getParameterMap();
        String[] tokens = (String[]) params.get(tokenName);
        String token;
        if ((tokens == null) || (tokens.length < 1)) {
            log.warn("Could not find token mapped to token name " + tokenName);
            return null;
        }

        token = tokens[0];
        return token;
    }

    /**
     * 从请求参数中获取token名字
     *
     * @return the token name found in the params, or null if it could not be found
     */
    public static String getTokenName(HttpServletRequest request) {
        Map params = request.getParameterMap();

        if (!params.containsKey(TOKEN_NAME_FIELD)) {
            log.warn("Could not find token name in params.");
            return null;
        }

        String[] tokenNames = (String[]) params.get(TOKEN_NAME_FIELD);
        String tokenName;

        if ((tokenNames == null) || (tokenNames.length < 1)) {
            log.warn("Got a null or empty token name.");
            return null;
        }
        tokenName = tokenNames[0];

        return tokenName;
    }

    /**
     * 验证当前请求参数中的token是否合法，如果合法的token出现就会删除它，它不会再次成功合法的token
     *
     * @return 验证结果
     */
    public static boolean validToken(HttpServletRequest request) {
//        String tokenName = getTokenName(request);
//        if (tokenName == null) {
//            log.debug("no token name found -> Invalid token ");
//            return false;
//        }
//
//        String token = getToken(request, tokenName);
//        if (token == null) {
//            if (log.isDebugEnabled()) {
//                log.debug("no token found for token name " + tokenName + " -> Invalid token ");
//            }
//            return false;
//        }
        String token = request.getParameter(TOKEN_NAME_FIELD);

        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
//        HttpSession session = request.getSession(false);
        String cacheToken = (String) session.getAttribute(TOKEN_NAME_FIELD);
        if (!token.equals(cacheToken)) {
            log.warn("zhb.token Form token " + token + " does not match the session token " + cacheToken + ".");
            return false;
        }
        // remove the token so it won't be used again
//        session.removeAttribute(cacheToken);
        session.removeAttribute(TOKEN_NAME_FIELD);

        return true;
    }

    public static String generateGUID() {
        return new BigInteger(165, RANDOM).toString(36).toUpperCase();
    }
}
