package com.movision.controller.app;

import com.movision.common.Response;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 首页新增接口
 */
@RestController
@RequestMapping("/rest/home/site/")
public class NewController {

    @ApiOperation(value = "dddd", notes = "ddddd", response = Response.class)
    @RequestMapping(value = "sel_three_serviceList", method = RequestMethod.GET)
    public Response sel_witkey_service(@ApiParam(value = "条数") @RequestParam int count) {
        Response Response = new Response();

        Response.setData("ddddd");
        return Response;
    }


}
