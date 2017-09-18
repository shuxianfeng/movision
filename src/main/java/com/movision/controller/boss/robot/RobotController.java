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
}
