package com.movision.controller.app2;

import com.movision.common.Response;
import com.movision.facade.circle.CircleAppFacade;
import com.movision.facade.label.LabelFacade;
import com.movision.facade.user.UserFacade;
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.postLabel.entity.PostLabelVo;
import com.movision.mybatis.user.entity.UserVo;
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
 * @Author shuxf
 * @Date 2017/7/24 15:15
 * 美番2.0版本“我的”板块中所有二级页面接口（我的页面上半部分和下半部分接口在AppWaterfallController中）
 */
@RestController
@RequestMapping("/app/mine2/")
public class AppMineController {

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private CircleAppFacade circleAppFacade;

    @Autowired
    private LabelFacade labelFacade;

    @ApiOperation(value = "我的--关注--关注的圈子，点击关注调用的关注的圈子接口", notes = "关注的圈子部分、关注的作者部分、关注的标签部分", response = Response.class)
    @RequestMapping(value = "myfollowcircle", method = RequestMethod.POST)
    public Response getMineFollowCircle(@ApiParam(value = "用户id(必填，否则无法进入‘我的’页面)") @RequestParam String userid,
                                        @ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                        @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize){
        Response response = new Response();

        Paging<CircleVo> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List<CircleVo> myFollowCircleList = circleAppFacade.getMineFollowCircle(userid, pager);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
            response.setData(myFollowCircleList);
        }
        return response;
    }

    @ApiOperation(value = "我的--关注--关注的作者，点击关注调用的关注的作者接口", notes = "关注的作者部分", response = Response.class)
    @RequestMapping(value = "myfollowauthor", method = RequestMethod.POST)
    public Response getMineFollowAuthor(@ApiParam(value = "用户id(必填，否则无法进入‘我的’页面)") @RequestParam String userid,
                                        @ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                        @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize){
        Response response = new Response();

        Paging<UserVo> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List<UserVo> myFollowAuthorList = userFacade.getMineFollowAuthor(userid, pager);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
            response.setData(myFollowAuthorList);
        }
        return response;
    }

    @ApiOperation(value = "我的--关注--关注的标签，点击关注调用的关注的标签接口", notes = "关注的标签部分", response = Response.class)
    @RequestMapping(value = "myfollowlabel", method = RequestMethod.POST)
    public Response getMineFollowLabel(@ApiParam(value = "用户id(必填，否则无法进入‘我的’页面)") @RequestParam String userid,
                                       @ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                       @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize){
        Response response = new Response();

        Paging<PostLabelVo> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List<PostLabelVo> myFollowCircleList = labelFacade.getMineFollowLabel(userid, pager);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
            response.setData(myFollowCircleList);
        }
        return response;
    }

    @ApiOperation(value = "我的模块——点击粉丝，进入用户被关注的粉丝用户列表接口", notes = "用户被关注的粉丝用户列表接口", response = Response.class)
    @RequestMapping(value = "myfans", method = RequestMethod.POST)
    public Response getMyfans(@ApiParam(value = "用户id(必填，否则无法进入‘我的’页面)") @RequestParam String userid,
                              @ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                              @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize){
        Response response = new Response();

        Paging<UserVo> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List<UserVo> myFansList = userFacade.getMyfans(userid, pager);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
            response.setData(myFansList);
        }
        return response;
    }
}
