package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.index.FacadeIndex;
import com.movision.facade.index.FacadeSuggestion;
import com.movision.mybatis.submission.entity.Submission;
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
    @Autowired
    private FacadeSuggestion facadeSuggestion;

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

    @ApiOperation(value = "意见反馈接口", notes = "用户对平台的意见建议", response = Response.class)
    @RequestMapping(value = "suggestion", method = RequestMethod.POST)
    public Response insertSuggestion(@ApiParam(value = "用户id") @RequestParam String userid,
                                     @ApiParam(value = "选填手机号，qq号等") @RequestParam(required = false) String phone,
                                     @ApiParam(value = "反馈内容") @RequestParam String content) {
        Response response = new Response();
        facadeSuggestion.insertSuggestion(userid, phone, content);//用户反馈信息
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        return response;
    }

}
