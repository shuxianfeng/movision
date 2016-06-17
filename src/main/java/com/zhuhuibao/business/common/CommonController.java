package com.zhuhuibao.business.common;

import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.memCenter.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by cxx on 2016/6/17 0017.
 */
@RestController
@RequestMapping("/rest/common")
public class CommonController {

    @Autowired
    UploadService uploadService;

    @ApiOperation(value = "上传图片，返回url", notes = "上传图片，返回url", response = Response.class)
    @RequestMapping(value = "upload_img", method = RequestMethod.POST)
    public Response uploadImg(HttpServletRequest req) throws Exception {
        //完成文件上传
        Response result = new Response();
        String url = uploadService.upload(req,"img");
        result.setData(url);
        return result;
    }
}
