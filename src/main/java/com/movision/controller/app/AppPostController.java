package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.index.FacadePost;
import com.movision.mybatis.post.entity.PostVo;
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
 * @Date 2017/1/19 15:42
 */
@RestController
@RequestMapping("/app/post/")
public class AppPostController {

    @Autowired
    private FacadePost facadePost;

    @ApiOperation(value = "帖子详情数据返回接口", notes = "用于返回请求帖子详情内容", response = Response.class)
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    public Response queryPostDetail(@ApiParam(value = "帖子id") @RequestParam String postid,
                                    @ApiParam(value = "帖子类型：0 普通帖 1 原生视频帖( isactive为0时该字段不为空)") @RequestParam String type) {
        Response response = new Response();

        PostVo post = facadePost.queryPostDetail(postid, type);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(post);
        return response;
    }

    @ApiOperation(value = "查询往期3天的精选帖子列表", notes = "用于返回往期3天的精选帖子列表的接口", response = Response.class)
    @RequestMapping(value = "pastPost", method = RequestMethod.POST)
    public Response queryPastPostDetail(@ApiParam(value = "查询日期") @RequestParam(required = false) String date) {
        Response response = new Response();


        Map<String, Object> postmap = facadePost.queryPastPostDetail(date);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(postmap);
        return response;
    }


    @ApiOperation(value = "APP端发布普通帖子", notes = "用于APP端发布普通帖子的接口", response = Response.class)
    @RequestMapping(value = "releasePost", method = RequestMethod.POST)
    public Response releasePost(@ApiParam(value = "所属圈子id") @RequestParam String circleid,
                                @ApiParam(value = "帖子主标题") @RequestParam String title,
                                @ApiParam(value = "帖子内容") @RequestParam String postcontent,
                                @ApiParam(value = "是否为活动：0 帖子 1 活动") @RequestParam String isactive) {
        Response response = new Response();

        int count = facadePost.releasePost(circleid, title, postcontent, isactive);

        if (response.getCode() == 200 && count == 1) {
            response.setMessage("发布成功");
        }
        return response;
    }


    /**
     * 帖子点赞接口
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "更新帖子点赞次数", notes = "用于帖子点赞接口", response = Response.class)
    @RequestMapping(value = "updateZan", method = RequestMethod.POST)
    public Response updatePostByZanSum(@ApiParam(value = "帖子id") @RequestParam String id) {
        Response response = new Response();
        int sum = facadePost.updatePostByZanSum(id);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(sum);
        return response;
    }
}
