package com.movision.controller.vote;

import com.movision.common.Response;
import com.movision.facade.voteH5.VoteFacade;
import com.movision.mybatis.activeH5.entity.ActiveH5;
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.mybatis.take.entity.TakeVo;
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
 * @Author zhanglei
 * @Date 2017/8/22 14:43
 */
@RestController
@RequestMapping("/vote/wechat/")
public class VoteController {

    @Autowired
    private VoteFacade voteFacade;

    /**
     * 添加活动
     *
     * @param name
     * @param photo
     * @param begintime
     * @param endtime
     * @param
     * @param activitydescription
     * @return
     */
    @ApiOperation(value = "添加活动", notes = "添加活动", response = Response.class)
    @RequestMapping(value = "insertSelective", method = RequestMethod.POST)
    public Response insertSelective(@ApiParam(value = "活动名称") @RequestParam String name,
                                    @ApiParam(value = "活动图片") @RequestParam String photo,
                                    @ApiParam(value = "活动开始时间") @RequestParam String begintime,
                                    @ApiParam(value = "活动结束时间") @RequestParam String endtime,
                                    @ApiParam(value = "活动说明") @RequestParam String activitydescription) {
        Response response = new Response();
        int result = voteFacade.insertSelective(name, photo, begintime, endtime, activitydescription);
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        response.setData(result);
        return response;
    }


    /**
     * 删除活动
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除活动", notes = "删除活动", response = Response.class)
    @RequestMapping(value = "deleteActive", method = RequestMethod.POST)
    public Response deleteActive(@ApiParam(value = "活动id") @RequestParam int id) {
        Response response = new Response();
        int result = voteFacade.deleteActive(id);
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        response.setData(result);
        return response;
    }


/*    *//**
     * 查询全部活动
     *
     * @return
     *//*
    @ApiOperation(value = "查询全部活动", notes = "查询全部活动", response = Response.class)
    @RequestMapping(value = "findAllActive", method = RequestMethod.POST)
    public Response findAllActive(@ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                  @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<ActiveH5> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List result = voteFacade.findAllActive(pager);
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        pager.result(result);
        response.setData(pager);
        return response;
    }*/


    /**
     * 添加参赛人员
     *
     * @param activeid
     * @param name
     * @return
     */
    @ApiOperation(value = "添加参赛人员", notes = "添加参赛人员", response = Response.class)
    @RequestMapping(value = "insertSelectiveTP", method = RequestMethod.POST)
    public Response insertSelectiveTP(@ApiParam(value = "活动id") @RequestParam String activeid,
                                      @ApiParam(value = "投稿作品名称") @RequestParam String name,
                                      @ApiParam(value = "投稿人电话") @RequestParam String phone,
                                      @ApiParam(value = "投稿内容") @RequestParam String photo,
                                      @ApiParam(value = "投稿描述") @RequestParam(required = false) String describe,
                                      @ApiParam(value = "参赛人") @RequestParam String nickname) {
        Response response = new Response();
        int result = voteFacade.insertSelectiveTP(activeid, name, phone, photo, describe, nickname);
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        response.setData(result);
        return response;
    }


    /**
     * 删除参赛人员
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除参赛人员", notes = "删除参赛人员", response = Response.class)
    @RequestMapping(value = "deleteTakePeople", method = RequestMethod.POST)
    public Response deleteTakePeople(@ApiParam(value = "参赛人员id") @RequestParam int id) {
        Response response = new Response();
        int result = voteFacade.deleteTakePeople(id);
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        response.setData(result);
        return response;
    }


    /**
     * 查询全部参赛人员
     *
     * @return
     */
    @ApiOperation(value = "查询全部参赛人员", notes = "查询全部参赛人员", response = Response.class)
    @RequestMapping(value = "findAllTake", method = RequestMethod.POST)
    public Response findAllTake(@ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize,
                                @ApiParam(value = "编号") @RequestParam(required = false) String mark,
                                @ApiParam(value = "姓名") @RequestParam(required = false) String nickname) {
        Response response = new Response();
        Paging<TakeVo> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List result = voteFacade.findAllTakeCondition(pager, mark, nickname);
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        pager.result(result);
        response.setData(pager);
        return response;
    }
}
