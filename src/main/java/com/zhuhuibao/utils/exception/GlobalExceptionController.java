package com.zhuhuibao.utils.exception;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.utils.JsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016/5/5 0005.
 */
@Controller
public class GlobalExceptionController {

    /**
     * 请求异常 错误请求
     * @return
     * @throws Exception
     * String
     */
    @RequestMapping(value = "/error_400", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public void error_400(HttpServletRequest req, HttpServletResponse response) throws Exception {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setMsgCode(400);
        jsonResult.setMessage("错误请求,服务器不理解请求的语法");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 请求异常
     * @return
     * @throws Exception
     * String
     */
    @RequestMapping(value = "/error_403", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public void error_403(HttpServletRequest req, HttpServletResponse response) throws Exception {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setMsgCode(403);
        jsonResult.setMessage("（禁止） 服务器拒绝请求。");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 请求异常 服务器找不到请求的网页
     * @return
     * @throws Exception
     * String
     */
    @RequestMapping(value = "/error_404", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public void error_404(HttpServletRequest req, HttpServletResponse response) throws Exception {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setMsgCode(404);
        jsonResult.setMessage("服务器找不到请求的网页");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 禁用请求中指定的方法
     * @return
     * @throws Exception
     * String
     */
    @RequestMapping(value = "/error_405", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public void error_405(HttpServletRequest req, HttpServletResponse response) throws Exception {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setMsgCode(405);
        jsonResult.setMessage("禁用请求中指定的方法");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 禁用请求中指定的方法
     * @return
     * @throws Exception
     * String
     */
    @RequestMapping(value = "/error_413", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public void error_413(HttpServletRequest req, HttpServletResponse response) throws Exception {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setMsgCode(413);
        jsonResult.setMessage("（请求实体过大） 服务器无法处理请求，因为请求实体过大，超出服务器的处理能力。 ");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 服务器异常
     * @return
     * String
     */
    @RequestMapping(value ="/error_500", produces = "text/html;charset=UTF-8")
    public void error_500(HttpServletRequest req, HttpServletResponse response) throws Exception {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setMsgCode(500);
        jsonResult.setMessage("服务器处理失败");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

}
