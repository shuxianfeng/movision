package com.movision.controller.app;

import com.movision.common.Response;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author zhurui
 * @Date 2017/6/8 11:34
 */
@RestController
public class TestController {

    private static Logger log = LoggerFactory.getLogger(TestController.class);

    @ApiOperation(value = "测试配置文件加载", notes = "测试配置文件加载", response = Response.class)
    @RequestMapping(value = "app/test_PropertiesLoader", method = RequestMethod.GET)
    public Response testPropertiesLoader() {
        Response response = new Response();
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            log.debug("本机的IP = " + inetAddress.getHostAddress());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
//        String uploadMode = PropertiesLoader.getValue("upload.mode");


        return response;
    }
}
