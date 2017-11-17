package com.movision.controller.boss.robot;

import com.movision.common.Response;
import com.movision.facade.robot.RobotFacade;
import com.movision.mybatis.robotOperationJob.entity.RobotOperationJobPage;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author zhuangyuhao
 * @Date 2017/11/1 11:29
 */
@RestController
@RequestMapping("boss/robot/job")
public class RobotJobController {

    @Autowired
    private RobotFacade robotFacade;

    @ApiOperation(value = "查询机器人任务列表（分页）", notes = "查询机器人任务列表（分页）", response = Response.class)
    @RequestMapping(value = "query_robot_job_page", method = RequestMethod.GET)
    public Response queryRobotJobPage(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                      @RequestParam(required = false, defaultValue = "10") String pageSize,
                                      @ApiParam(value = "任务类型 。1：点赞，2：收藏，3：评论，4：关注") @RequestParam(required = false) Integer type) {
        Response response = new Response();
        Paging<RobotOperationJobPage> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<RobotOperationJobPage> list = robotFacade.findAllRobotJobPage(type, pager);
        pager.result(list);
        response.setData(pager);
        return response;
    }

    @ApiOperation(value = "改变任务为立即执行", notes = "改变任务为立即执行", response = Response.class)
    @RequestMapping(value = "execute_job_nexttime", method = RequestMethod.POST)
    public Response executeJobNexttime(@ApiParam(value = "任务id") @RequestParam(required = false) Integer jobid) {
        Response response = new Response();
        int result = robotFacade.changeJobexecuteImmediately(jobid);
        if (result == 1) {
            response.setCode(200);
            response.setMessage("执行成功");
        } else {
            response.setCode(400);
            response.setMessage("执行失败");
        }
        return response;
    }
}
