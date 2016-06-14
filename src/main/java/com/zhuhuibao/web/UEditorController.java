package com.zhuhuibao.web;

import com.baidu.ueditor.ActionEnter;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * UEditor上传
 */
@RestController
@RequestMapping("/rest/ueditor")
@Api(value = "ueditor", description = "百度上传")
public class UEditorController {
    private static final Logger log = LoggerFactory.getLogger(UEditorController.class);


    @RequestMapping(value="uploadimage", method = RequestMethod.POST)
    @ApiOperation(value = "百度上传", notes = "百度上传")
    public void uploadimage(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        request.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type" , "text/html");
        response.setContentType("text/html;charset=utf-8");

        String rootPath = request.getSession().getServletContext().getRealPath("/");
        System.out.println("rootPath:" + rootPath);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String outHtml = new ActionEnter(request, rootPath).exec();
            System.out.println("#####" + outHtml);
            out.write(outHtml);

        } catch (IOException e) {
            e.printStackTrace();
            log.error("获取输出流异常:" + e.getMessage());
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }
}
