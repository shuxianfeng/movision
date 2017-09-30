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
 * 机器人对APP用户的一些操作
 *
 * @Author zhuangyuhao
 * @Date 2017/9/30 10:09
 */
@RestController
@RequestMapping("boss/robot/appuser")
public class RobotAppuserController {
    @Autowired
    private RobotFacade robotFacade;

    @ApiOperation(value = "关注单个用户", notes = "关注单个用户", response = Response.class)
    @RequestMapping(value = "/single_robot_follow_user", method = RequestMethod.POST)
    public Response robotFollowUser(@ApiParam(value = "需要调用的机器人数量") @RequestParam Integer num,
                                    @ApiParam(value = "被关注的用户id") @RequestParam Integer userid) throws IOException {
        Response response = new Response();
        robotFacade.robotFollowUser(userid, num);
        return response;
    }

    @ApiOperation(value = "批量关注用户", notes = "批量关注用户", response = Response.class)
    @RequestMapping(value = "/batch_follow_user", method = RequestMethod.POST)
    public Response batchFollowUser(@ApiParam(value = "需要调用的机器人的最大数量, 每次对帖子操作的机器人数量是[0,num)的随机数") @RequestParam Integer num,
                                    @ApiParam(value = "被关注的用户id,用逗号分隔") @RequestParam String userids) throws IOException {
        Response response = new Response();
        robotFacade.batchFollowUser(userids, num);
        return response;
    }
}
