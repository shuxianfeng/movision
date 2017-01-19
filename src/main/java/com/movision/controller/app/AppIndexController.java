package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.index.FacadeIndex;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * @Author shuxf
 * @Date 2017/1/17 15:25
 */
@RestController
@RequestMapping("/app/index/")
public class AppIndexController {

    @Autowired
    private FacadeIndex facadeIndex;

    @ApiOperation(value = "首页数据返回接口", notes = "用户返回首页整版数据", response = Response.class)
    @RequestMapping(value = "index", method = RequestMethod.POST)
    public Response queryIndexData(@ApiParam(value = "用户id") @RequestParam(required = false) String userid) {
        Response response = new Response();

        Map<String, Object> map = facadeIndex.queryIndexData(userid);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(map);
        return response;
    }


}
