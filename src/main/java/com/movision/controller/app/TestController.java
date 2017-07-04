package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.upload.UploadFacade;
import com.movision.utils.oss.UploadUtil;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 这是一个用于测试的控制器
 *
 *
 * @Author zhurui
 * @Date 2017/6/8 11:34
 */
@RestController
public class TestController {

    @Autowired
    private UploadFacade uploadFacade;

    @ApiOperation(value = "测试配置文件加载", notes = "测试配置文件加载", response = Response.class)
    @RequestMapping(value = "app/test_PropertiesLoader", method = RequestMethod.GET)
    public Response testPropertiesLoader() {
        Response response = new Response();
        response.setData(uploadFacade.getConfigVar("post.incise.domain"));
        return response;
    }
}
