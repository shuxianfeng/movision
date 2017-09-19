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
 * @Author zhuangyuhao
 * @Date 2017/9/18 10:48
 */
@RestController
@RequestMapping("boss/robot")
public class RobotController {

    @Autowired
    private RobotFacade robotFacade;

    @ApiOperation(value = "创建机器人", notes = "创建机器人", response = Response.class)
    @RequestMapping(value = "/create_robot", method = RequestMethod.POST)
    public Response createRobot(@ApiParam(value = "需要创建的机器人数量") @RequestParam Integer num) throws IOException {
        Response response = new Response();
        robotFacade.batchAddRobotUser(num);
        return response;
    }

    @ApiOperation(value = "对指定的帖子进行指定机器人的点赞操作", notes = "对指定的帖子进行指定机器人的点赞操作", response = Response.class)
    @RequestMapping(value = "/robot_zan_post", method = RequestMethod.POST)
    public Response robotZanPost(@ApiParam(value = "需要调用的机器人数量") @RequestParam Integer num,
                                 @ApiParam(value = "帖子id") @RequestParam Integer postid) throws IOException {
        Response response = new Response();
        robotFacade.robotZanPost(postid, num);
        return response;
    }

    @ApiOperation(value = "机器人收藏帖子操作", notes = "机器人收藏帖子操作", response = Response.class)
    @RequestMapping(value = "/robot_collect_post", method = RequestMethod.POST)
    public Response robotCollectPost(@ApiParam(value = "需要调用的机器人数量") @RequestParam Integer num,
                                     @ApiParam(value = "帖子id") @RequestParam Integer postid) throws IOException {
        Response response = new Response();
        robotFacade.robotCollectPost(postid, num);
        return response;
    }

    @ApiOperation(value = "机器人关注用户操作", notes = "机器人关注用户操作", response = Response.class)
    @RequestMapping(value = "/robot_follow_user", method = RequestMethod.POST)
    public Response robotFollowUser(@ApiParam(value = "需要调用的机器人数量") @RequestParam Integer num,
                                    @ApiParam(value = "被关注的用户id") @RequestParam Integer userid) throws IOException {
        Response response = new Response();
        robotFacade.robotFollowUser(userid, num);
        return response;
    }



}
