package com.movision.controller.web;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.movision.common.Response;


/**
 * 登录
 *
 * @author zhuangyuhao@20160303
 */
@RestController
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @RequestMapping(value = "/rest/web/authc", method = RequestMethod.GET)
    public Response isLogin() throws IOException {
        Response response = new Response();
        response.setData("1111");
        return response;

    }


}
