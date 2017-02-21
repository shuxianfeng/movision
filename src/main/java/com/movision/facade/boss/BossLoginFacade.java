package com.movision.facade.boss;

import com.movision.common.Response;
import com.movision.utils.JsonUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/21 13:54
 */
@Service
public class BossLoginFacade {

    public void handleNoPermission(HttpServletResponse response) throws IOException {
        Response result = new Response();
        result.setCode(401);
        result.setMessage("请求无权限！");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    public void handleNotLogin(HttpServletResponse response) throws IOException {
        Response result = new Response();
        result.setCode(401);
        result.setMessage("请登录！");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

}
