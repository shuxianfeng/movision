package com.movision.controller.boss.robot;

import com.movision.common.Response;
import com.movision.facade.robot.RobotFacade;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 机器人对帖子的一系列操作
 *
 * @Author zhuangyuhao
 * @Date 2017/9/30 10:05
 */
@RestController
@RequestMapping("boss/robot/post")
public class RobotPostController {
    @Autowired
    private RobotFacade robotFacade;

    /**
     * 利用机器人为帖子制造评论接口
     *
     * @param postid
     * @param number
     * @return
     */
    @ApiOperation(value = "单个帖子评论", notes = "单个帖子评论", response = Response.class)
    @RequestMapping(value = "single_post_comment", method = RequestMethod.POST)
    public Response insertPostCommentByRobolt(@ApiParam(value = "帖子id") @RequestParam Integer postid,
                                              @ApiParam(value = "使用机器人数量") @RequestParam Integer number) {
        Response response = new Response();
        robotFacade.insertPostCommentByRobolt(postid, number);
        response.setMessage("操作成功");
        response.setData(1);
        return response;
    }

    @ApiOperation(value = "批量评论帖子", notes = "批量评论帖子", response = Response.class)
    @RequestMapping(value = "batch_post_comment", method = RequestMethod.POST)
    public Response batchPostComment(@ApiParam(value = "需要调用的机器人的最大数量, 每次对帖子操作的机器人数量是[0,num)的随机数") @RequestParam Integer num,
                                     @ApiParam(value = "帖子id, 以逗号分隔") @RequestParam String postids) {
        Response response = new Response();
        robotFacade.robotCommentBatchPost(postids, num);
        response.setMessage("操作成功");
        response.setData(1);
        return response;
    }

    @ApiOperation(value = "单个点赞帖子", notes = "单个点赞帖子", response = Response.class)
    @RequestMapping(value = "/single_post_zan", method = RequestMethod.POST)
    public Response robotZanPost(@ApiParam(value = "需要调用的机器人数量") @RequestParam Integer num,
                                 @ApiParam(value = "帖子id") @RequestParam Integer postid) throws IOException {
        Response response = new Response();
        robotFacade.robotZanPost(postid, num);
        response.setMessage("操作成功");
        return response;
    }

    @ApiOperation(value = "批量点赞帖子", notes = "批量点赞帖子", response = Response.class)
    @RequestMapping(value = "/batch_post_zan", method = RequestMethod.POST)
    public Response robotZanBatchPost(@ApiParam(value = "需要调用的机器人的最大数量, 每次对帖子操作的机器人数量是[0,num)的随机数") @RequestParam Integer num,
                                      @ApiParam(value = "帖子id, 以逗号分隔") @RequestParam String postids) throws IOException {
        Response response = new Response();
        robotFacade.robotZanBatchPost(postids, num);
        response.setMessage("操作成功");
        return response;
    }

    @ApiOperation(value = "批量收藏帖子", notes = "批量收藏帖子", response = Response.class)
    @RequestMapping(value = "/batch_post_collect", method = RequestMethod.POST)
    public Response robotCollectBatchPost(@ApiParam(value = "需要调用的机器人的最大数量, 每次对帖子操作的机器人数量是[0,num)的随机数") @RequestParam Integer num,
                                          @ApiParam(value = "帖子id, 以逗号分隔") @RequestParam String postids) throws IOException {
        Response response = new Response();
        robotFacade.robotCollectBatchPost(postids, num);
        response.setMessage("操作成功");
        return response;
    }

    @ApiOperation(value = "单个帖子收藏", notes = "单个帖子收藏", response = Response.class)
    @RequestMapping(value = "/single_post_collect", method = RequestMethod.POST)
    public Response robotCollectPost(@ApiParam(value = "需要调用的机器人数量") @RequestParam Integer num,
                                     @ApiParam(value = "帖子id") @RequestParam Integer postid) throws IOException {
        Response response = new Response();
        robotFacade.robotCollectPost(postid, num);
        return response;
    }

    @ApiOperation(value = "批量帖子机器人点赞、收藏、评论操作", notes = "批量帖子机器人点赞、收藏、评论操作", response = Response.class)
    @RequestMapping(value = "/batch_robot_action_with_zan_collect_comment", method = RequestMethod.POST)
    public Response robotActionWithZanCollectComment(@ApiParam(value = "需要调用的机器人的最大数量, 每次对帖子操作的机器人数量是[0,num)的随机数") @RequestParam Integer num,
                                                     @ApiParam(value = "帖子id, 以逗号分隔") @RequestParam String postids) throws IOException {
        Response response = new Response();
        robotFacade.robotActionWithZanCollectComment(postids, num);
        response.setMessage("操作成功");
        return response;
    }
}
