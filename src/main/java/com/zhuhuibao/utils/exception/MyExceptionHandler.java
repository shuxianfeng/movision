package com.zhuhuibao.utils.exception;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.utils.JsonUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/5 0005.
 */
@ControllerAdvice
public class MyExceptionHandler{

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public JsonResult defaultErrorHandler(HttpServletRequest req,HttpServletResponse response, Exception e) throws Exception {
        BusinessException ex = (BusinessException) e;
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(400);
        jsonResult.setMessage(ex.getMessage());
        jsonResult.setMsgCode(ex.getMsgid());
        return jsonResult;
        /*response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));*/
    }
}
