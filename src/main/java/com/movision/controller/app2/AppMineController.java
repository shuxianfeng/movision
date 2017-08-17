package com.movision.controller.app2;

import com.movision.common.Response;
import com.movision.facade.circle.CircleAppFacade;
import com.movision.facade.label.LabelFacade;
import com.movision.facade.user.UserFacade;
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.postLabel.entity.GeographicLabel;
import com.movision.mybatis.postLabel.entity.PostLabelVo;
import com.movision.mybatis.user.entity.InviteUserVo;
import com.movision.mybatis.user.entity.UserVo;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

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
        pager.result(myFollowCircleList);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
            response.setData(pager);
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
        pager.result(myFollowAuthorList);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
            response.setData(pager);
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
        pager.result(myFollowCircleList);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
            response.setData(pager);
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
        pager.result(myFansList);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
            response.setData(pager);
        }
        return response;
    }

    @ApiOperation(value = "我的模块——邀请好友，选择邀请渠道后的子页面上半部分数据返回接口", notes = "邀请送现金页面上半部分数据接口", response = Response.class)
    @RequestMapping(value = "myinvite", method = RequestMethod.POST)
    public Response myinvite(@ApiParam(value = "用户id(必填，否则无法进入‘我的’页面)") @RequestParam String userid){
        Response response = new Response();

        Map<String, Object> map = userFacade.myinvite(userid);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
            response.setData(map);
        }
        return response;
    }

    @ApiOperation(value = "我的模块——邀请好友，选择邀请渠道后的子页面下半部分数据返回接口", notes = "邀请送现金页面下半部分数据接口", response = Response.class)
    @RequestMapping(value = "myInviteList", method = RequestMethod.POST)
    public Response myInviteList(@ApiParam(value = "用户id(必填，否则无法进入‘我的’页面)") @RequestParam String userid,
                                 @ApiParam(value = "查询类型：0 我的邀请 1 排行榜") @RequestParam String type,
                                 @ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                 @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize){
        Response response = new Response();

        Paging<InviteUserVo> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List<InviteUserVo> inviteUserList = userFacade.myInviteList(userid, type, pager);
        pager.result(inviteUserList);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
            response.setData(pager);
        }
        return response;
    }

    @ApiOperation(value = "我的模块--某某的达人之路接口（含等级升级、徽章和升级经验的获取）", notes = "某某的达人之路页面数据返回", response = Response.class)
    @RequestMapping(value = "myTalentInfo", method = RequestMethod.POST)
    public Response myTalentInfo(@ApiParam(value = "用户id(必填，否则无法进入‘我的’页面)") @RequestParam String userid){
        Response response = new Response();

        Map<String, Object> map = userFacade.myTalentInfo(userid);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
            response.setData(map);
        }

        return response;
    }

    @ApiOperation(value = "我的模块--点击小脚印--进入用户足迹地图页面接口", notes = "返回当前用户足迹地图中所有的地理标签位置", response = Response.class)
    @RequestMapping(value = "getfootmap", method = RequestMethod.POST)
    public Response getfootmap(@ApiParam(value = "用户id(必填，否则无法进入‘我的’页面)") @RequestParam String userid) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Response response = new Response();

        List<GeographicLabel> geographicList = labelFacade.getfootmap(userid);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
            response.setData(geographicList);
        }

        return response;
    }
}
