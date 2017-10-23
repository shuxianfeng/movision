package com.movision.controller.boss.robot;

import com.movision.common.Response;
import com.movision.facade.robot.RobotFacade;
import com.movision.mybatis.user.entity.User;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 机器人自身的管理
 *
 * @Author zhuangyuhao
 * @Date 2017/9/18 10:48
 */
@RestController
@RequestMapping("boss/robot/robot_self")
public class RobotSelfController {

    @Autowired
    private RobotFacade robotFacade;

    @ApiOperation(value = "创建机器人", notes = "创建机器人", response = Response.class)
    @RequestMapping(value = "/create_robot", method = RequestMethod.POST)
    public Response createRobot(@ApiParam(value = "需要创建的机器人数量") @RequestParam Integer num) throws IOException {
        Response response = new Response();
        robotFacade.batchAddRobotUser(num);
        response.setMessage("操作成功");
        return response;
    }

    @ApiOperation(value = "用于查询机器人列表接口", notes = "用于查询机器人列表接口", response = Response.class)
    @RequestMapping(value = "/query_robot_list", method = RequestMethod.POST)
    public Response QueryRobotByList(@ApiParam(value = "机器人名称") @RequestParam(required = false) String name,
                                     @ApiParam(value = "当前页") @RequestParam(defaultValue = "1") String pageNo,
                                     @ApiParam(value = "每页几条") @RequestParam(defaultValue = "10") String pageSize) {
        Response response = new Response();
        List<User> userVoList = new ArrayList<User>();
        Paging<User> pag = new Paging<User>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        //查询机器人列表,用于列表查询
        userVoList = robotFacade.QueryRobotByList(name, pag);
        pag.result(userVoList);
        response.setData(pag);
        response.setMessage("查询成功");
        return response;
    }

    @ApiOperation(value = "用于查询机器人详情接口", notes = "用于查询机器人详情接口", response = Response.class)
    @RequestMapping(value = "/query_robot_detail", method = RequestMethod.POST)
    public Response queryRobotById(@ApiParam(value = "机器人id") @RequestParam String id) {
        Response response = new Response();
        User user = robotFacade.queryRobotById(id);
        response.setMessage("查询成功");
        response.setData(user);
        return response;
    }

    @ApiOperation(value = "更新机器人", notes = "更新机器人", response = Response.class)
    @RequestMapping(value = "/update_robot", method = RequestMethod.POST)
    public Response updateRoboltById(@ApiParam(value = "id") @RequestParam(required = false) String id,
                                     @ApiParam(value = "邮箱") @RequestParam(required = false) String email,
                                     @ApiParam(value = "nickname") @RequestParam(required = false) String nickname,
                                     @ApiParam(value = "手机号") @RequestParam(required = false) String phone,
                                     @ApiParam(value = "头像") @RequestParam(required = false) String photo,
                                     @ApiParam(value = "性别") @RequestParam(required = false) String sex) {
        Response response = new Response();
        robotFacade.updateRoboltById(id, email, nickname, phone, photo, sex);
        response.setMessage("操作成功");
        response.setData(1);
        return response;
    }

    @ApiOperation(value = "批量替换机器人的头像", notes = "批量替换机器人的头像", response = Response.class)
    @RequestMapping(value = "batch_change_robot_photo", method = RequestMethod.POST)
    public Response batchChangeRobotPhoto(@ApiParam(value = "机器人id，逗号分隔") @RequestParam String userids) {
        Response response = new Response();
        robotFacade.batchChangeRobotPhoto(userids);
        response.setMessage("操作成功");
        return response;
    }

    @ApiOperation(value = "批量替换机器人的昵称", notes = "批量替换机器人的昵称", response = Response.class)
    @RequestMapping(value = "batch_change_robot_nickname", method = RequestMethod.POST)
    public Response batchChangeRobotNickname(@ApiParam(value = "机器人id，逗号分隔") @RequestParam String userids) {
        Response response = new Response();
        robotFacade.batchChangeRobotNickname(userids);
        response.setMessage("操作成功");
        return response;
    }

    @ApiOperation(value = "替换全部机器人的昵称、头像、签名", notes = "批量替换机器人的昵称、头像、签名", response = Response.class)
    @RequestMapping(value = "all_change_robot_info", method = RequestMethod.POST)
    public Response allChangeRobotNickname() {
        Response response = new Response();
        robotFacade.allChangeRobotInfo();
        response.setMessage("操作成功");
        return response;
    }

}
