package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.index.FacadeIndex;
import com.movision.mybatis.post.entity.PostVo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @Author shuxf
 * @Date 2017/1/17 15:25
 */
@RestController
@RequestMapping("/index/")
public class AppIndex {

    @Autowired
    private FacadeIndex facadeIndex;

    @ApiOperation(value = "首页数据返回接口", notes = "首页数据返回接口", response = Response.class)
    @RequestMapping(value = "index", method = RequestMethod.POST)
    public Response queryIndexData(@ApiParam(value = "条数") @RequestParam int count) {
        Response response = new Response();
        List<PostVo> postlist = facadeIndex.queryIndexData();
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(postlist);
        return response;
    }


}
