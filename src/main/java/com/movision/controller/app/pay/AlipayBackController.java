package com.movision.controller.app.pay;

import com.movision.common.Response;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author shuxf
 * @Date 2017/3/14 15:09
 */
@RestController
@RequestMapping("/app/alipayback/")
public class AlipayBackController {

    /**
     * 支付宝支付回调接口
     */
    @ApiOperation(value = "支付宝支付回调接口", notes = "用于支付宝支付的服务器端异步回调接口", response = Response.class)
    @RequestMapping(value = "alipayback", method = RequestMethod.POST)
    public Response alipayback() {
        Response response = new Response();


        return response;
    }
}
