package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.index.FacadeDiscover;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/1/18 17:47
 */
@RestController
@RequestMapping("/app/discover/")
public class AppDiscoverController {

    @Autowired
    private FacadeDiscover facadeDiscover;

    @ApiOperation(value = "发现页数据返回接口", notes = "用于返回发现页首页的全版数据", response = Response.class)
    @RequestMapping(value = "index", method = RequestMethod.POST)
    public Response queryIndexData() {
        Response response = new Response();

        Map<String, Object> map = facadeDiscover.queryDiscoverIndexData();

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(map);
        return response;
    }
}
