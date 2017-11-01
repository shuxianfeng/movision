package com.movision.controller.boss.robot;

import com.movision.common.Response;
import com.movision.common.constant.RobotConstant;
import com.movision.facade.index.FacadePost;
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

    @Autowired
    private FacadePost facadePost;

    /**
     * 利用机器人为帖子制造评论接口
     * 注：一个机器人可以对同一个帖子评论多次
     *
     * @param postid
     * @param number
     * @return
     */
    @ApiOperation(value = "新增机器人评论帖子任务", notes = "新增机器人评论帖子任务", response = Response.class)
    @RequestMapping(value = "single_post_comment", method = RequestMethod.POST)
    public Response insertPostCommentByRobolt(@ApiParam(value = "帖子id") @RequestParam Integer postid,
                                              @ApiParam(value = "使用机器人数量") @RequestParam Integer number,
                                              @ApiParam(value = "帖子主题，1：人像， 2：风光") @RequestParam Integer theme) {
        Response response = new Response();
        robotFacade.addSingleRobotJobProcess(number, postid, theme, RobotConstant.ROBOT_JOB_TYPE.comment_post.getCode(), 0);
        response.setMessage("操作成功");
        response.setData(1);
        return response;
    }

    @ApiOperation(value = "批量新增机器人评论帖子任务", notes = "批量新增机器人评论帖子任务", response = Response.class)
    @RequestMapping(value = "batch_post_comment", method = RequestMethod.POST)
    public Response batchPostComment(@ApiParam(value = "需要调用的机器人的最大数量, 每次对帖子操作的机器人数量是[0,num)的随机数") @RequestParam Integer num,
                                     @ApiParam(value = "帖子id, 以逗号分隔") @RequestParam String postids,
                                     @ApiParam(value = "这些帖子的主题，1：人像， 2：风光") @RequestParam Integer theme) {
        Response response = new Response();
        robotFacade.robotCommentBatchPost(postids, num, theme);
        response.setMessage("操作成功");
        response.setData(1);
        return response;
    }

    @ApiOperation(value = "新增机器人点赞帖子任务", notes = "新增机器人点赞帖子任务", response = Response.class)
    @RequestMapping(value = "/single_post_zan", method = RequestMethod.POST)
    public Response robotZanPost(@ApiParam(value = "需要调用的机器人数量") @RequestParam Integer number,
                                 @ApiParam(value = "帖子id") @RequestParam Integer postid) throws IOException {
        Response response = new Response();
        robotFacade.addSingleRobotJobProcess(number, postid, null, RobotConstant.ROBOT_JOB_TYPE.zan_post.getCode(), 0);
        response.setMessage("操作成功");
        return response;
    }

    @ApiOperation(value = "批量新增机器人点赞帖子任务", notes = "批量新增机器人点赞帖子任务", response = Response.class)
    @RequestMapping(value = "/batch_post_zan", method = RequestMethod.POST)
    public Response robotZanBatchPost(@ApiParam(value = "需要调用的机器人的最大数量, 每次对帖子操作的机器人数量是[0,num)的随机数") @RequestParam Integer num,
                                      @ApiParam(value = "帖子id, 以逗号分隔") @RequestParam String postids) throws IOException {
        Response response = new Response();
        robotFacade.robotZanBatchPost(postids, num);
        response.setMessage("操作成功");
        return response;
    }

    @ApiOperation(value = "批量新增机器人收藏帖子任务", notes = "批量新增机器人收藏帖子任务", response = Response.class)
    @RequestMapping(value = "/batch_post_collect", method = RequestMethod.POST)
    public Response robotCollectBatchPost(@ApiParam(value = "需要调用的机器人的最大数量, 每次对帖子操作的机器人数量是[0,num)的随机数") @RequestParam Integer num,
                                          @ApiParam(value = "帖子id, 以逗号分隔") @RequestParam String postids) throws IOException {
        Response response = new Response();
        robotFacade.robotCollectBatchPost(postids, num);
        response.setMessage("操作成功");
        return response;
    }

    @ApiOperation(value = "新增机器人收藏帖子任务", notes = "新增机器人收藏帖子任务", response = Response.class)
    @RequestMapping(value = "/single_post_collect", method = RequestMethod.POST)
    public Response robotCollectPost(@ApiParam(value = "需要调用的机器人数量") @RequestParam Integer number,
                                     @ApiParam(value = "帖子id") @RequestParam Integer postid) throws IOException {
        Response response = new Response();
        robotFacade.addSingleRobotJobProcess(number, postid, null, RobotConstant.ROBOT_JOB_TYPE.collect_post.getCode(), 0);
        return response;
    }

    /*@ApiOperation(value = "批量帖子机器人点赞、收藏、评论操作", notes = "批量帖子机器人点赞、收藏、评论操作", response = Response.class)
    @RequestMapping(value = "/batch_robot_action_with_zan_collect_comment", method = RequestMethod.POST)
    public Response robotActionWithZanCollectComment(@ApiParam(value = "需要调用的机器人的最大数量, 每次对帖子操作的机器人数量是[0,num)的随机数") @RequestParam Integer num,
                                                     @ApiParam(value = "帖子id, 以逗号分隔") @RequestParam String postids) throws IOException {
        Response response = new Response();
        robotFacade.robotActionWithZanCollectComment(postids, num);
        response.setMessage("操作成功");
        return response;
    }*/

    /**
     * 注：一个机器人可以对同一个帖子浏览多次
     * @param num
     * @return
     * @throws IOException
     */
    /*@ApiOperation(value = "操作机器人浏览APP中全部帖子", notes = "操作机器人浏览APP中全部帖子", response = Response.class)
    @RequestMapping(value = "/batch_view_post", method = RequestMethod.POST)
    public Response robotViewPost(@ApiParam(value = "需要调用的机器人数量") @RequestParam Integer num) throws IOException {
        Response response = new Response();
        robotFacade.insertMongoPostView(num);
        return response;
    }*/

    /*@ApiOperation(value = "操作机器人浏览某个用户的所有帖子", notes = "操作机器人浏览某个用户的所有帖子", response = Response.class)
    @RequestMapping(value = "/add_someone_post_view", method = RequestMethod.POST)
    public Response addSomeonePostView(@ApiParam(value = "需要调用的机器人数量") @RequestParam Integer num,
                                       @ApiParam(value = "被浏览的帖子的作者") @RequestParam Integer uid) throws IOException {
        Response response = new Response();
        robotFacade.insertSomeonePostView(num, uid);
        return response;
    }*/

//    @ApiOperation(value = "更新线上帖子的所有热度值", notes = "更新线上帖子的所有热度值", response = Response.class)
//    @RequestMapping(value = "/update_online_post_heatvalue", method = RequestMethod.POST)
//    public Response updateOnlinePostHeatvalue() throws IOException {
//        Response response = new Response();
//        facadePost.updateOnlinePostHeatvalue();
//        return response;
//    }
}
