package com.zhuhuibao.security.resubmit;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.utils.UUIDGenerator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author jianglz
 * @since 16/6/25.
 */
public class AvoidDuplicateSubmissionInterceptor extends HandlerInterceptorAdapter {

    public static final String TOKEN_NAME_FIELD = "resToken";


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            AvoidDuplicateSubmission annotation = method.getAnnotation(AvoidDuplicateSubmission.class);
            if (annotation != null) {
                boolean needSaveSession = annotation.saveToken();
                if (needSaveSession) {
                    Subject currentUser = SecurityUtils.getSubject();
                    Session session = currentUser.getSession(false);
                    session.setAttribute(TOKEN_NAME_FIELD, UUIDGenerator.genUUID());
                }

                boolean needRemoveSession = annotation.removeToken();
                if (needRemoveSession) {
                    if (isRepeatSubmit(request)) {
                        Response result = new Response();
                        result.setCode(400);
                        result.setMessage("请不要频繁操作");
                        writeMessageUtf8(response, result);
                        return false;
                    }
                    Subject currentUser = SecurityUtils.getSubject();
                    Session session = currentUser.getSession(false);
                    session.removeAttribute(TOKEN_NAME_FIELD);
                }
            }
        }
        return true;
    }

    private boolean isRepeatSubmit(HttpServletRequest request) {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        String serverToken = (String) session.getAttribute(TOKEN_NAME_FIELD);
        if (serverToken == null) {
            return true;
        }
        String clinetToken = request.getParameter(TOKEN_NAME_FIELD);
        return clinetToken == null || !serverToken.equals(clinetToken);
    }

    private void writeMessageUtf8(HttpServletResponse response, Response json) throws IOException {
        try {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(JsonUtils.getJsonStringFromObj(json));
        } finally {
            response.getWriter().close();
        }
    }
}
