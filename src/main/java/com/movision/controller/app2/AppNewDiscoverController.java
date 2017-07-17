package com.movision.controller.app2;

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
 * @Date 2017/7/17 20:54
 * 用户返回美番2.0发现页所有数据的接口控制器
 */
@RestController
@RequestMapping("/app/discover2/")
public class AppNewDiscoverController {
    @Autowired
    private FacadeDiscover facadeDiscover;

    @ApiOperation(value = "美番2.0发现页上半部分数据返回接口", notes = "用于返回发现页首页的全版数据", response = Response.class)
    @RequestMapping(value = "index", method = RequestMethod.POST)
    public Response getDiscoverIndex(){
        Response response = new Response();

        Map<String, Object> map = facadeDiscover.queryDiscoverIndexData2Up();
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(map);
        return response;
    }
}
