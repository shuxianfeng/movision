package com.movision.controller.boss.robot;

import com.movision.common.Response;
import com.movision.facade.robot.RobotFacade;
import com.movision.mybatis.robotComment.entity.RobotComment;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 机器人的评论管理（即管理评论字典表）
 *
 * @Author zhuangyuhao
 * @Date 2017/9/30 10:02
 */
@RestController
@RequestMapping("boss/robot/comment_dict")
public class RobotCommentDictController {

    @Autowired
    private RobotFacade robotFacade;

    //**********************************************评论操作

    @ApiOperation(value = "查询评论列表", notes = "用于查询机器人评论列表", response = Response.class)
    @RequestMapping(value = "query_robolt_comment", method = RequestMethod.POST)
    public Response queryRoboltComment(@ApiParam(value = "评论类型") @RequestParam(required = false) String type,
                                       @ApiParam(value = "当前页") @RequestParam(defaultValue = "1") String pageNo,
                                       @ApiParam(value = "每页几条") @RequestParam(defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<RobotComment> pag = new Paging(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Integer commentType = null;
        if (StringUtils.isNotEmpty(type)) {
            commentType = Integer.valueOf(type);
        }
        List<RobotComment> list = robotFacade.findAllQueryRoboltComment(commentType, pag);
        pag.result(list);
        response.setMessage("查询成功");
        response.setData(list);
        return response;
    }

    /**
     * 新增机器人评论
     *
     * @param comment
     * @return
     */
    @ApiOperation(value = "新增机器人评论", notes = "新增机器人评论", response = Response.class)
    @RequestMapping(value = "add_robot_comment", method = RequestMethod.POST)
    public Response insertRoboltComment(@ApiParam(value = "评论") @RequestParam String comment,
                                        @ApiParam(value = "评论类型") @RequestParam String type) {
        Response response = new Response();
        Map map = robotFacade.insertRoboltComment(comment, type);
        if (map.get("code").equals(200)) {
            response.setMessage("操作成功");
            response.setData(1);
        } else {
            response.setMessage("评论重复");
            response.setData(-1);
        }
        return response;
    }

    /**
     * 删除机器人评论
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除机器人评论", notes = "删除机器人评论", response = Response.class)
    @RequestMapping(value = "delete_comment", method = RequestMethod.POST)
    public Response deleteRoboltComment(@ApiParam(value = "评论id") @RequestParam String id) {
        Response response = new Response();
        robotFacade.deleteRoboltComment(id);
        response.setMessage("操作成功");
        response.setData(1);
        return response;
    }

    @ApiOperation(value = "根据评论id查询机器人评论", notes = "根据评论id查询机器人评论", response = Response.class)
    @RequestMapping(value = "query_comment_detail", method = RequestMethod.POST)
    public Response queryRoboltCommentById(@ApiParam(value = "评论id") @RequestParam String id) {
        Response response = new Response();
        RobotComment comment = robotFacade.queryRoboltCommentById(Integer.parseInt(id));
        response.setMessage("查询成功");
        response.setData(comment);
        return response;
    }

    /**
     * 更新机器人评论
     *
     * @param id
     * @param content
     * @param type
     * @return
     */
    @ApiOperation(value = "更新机器人评论", response = Response.class)
    @RequestMapping(value = "update_robot_comment", method = RequestMethod.POST)
    public Response updateRoboltComent(@ApiParam(value = "评论id") @RequestParam String id,
                                       @ApiParam(value = "评论内容") @RequestParam String content,
                                       @ApiParam(value = "评论类型") @RequestParam String type) {
        Response response = new Response();
        robotFacade.updateRoboltComent(id, content, type);
        response.setMessage("操作成功");
        response.setData(1);
        return response;
    }
}
