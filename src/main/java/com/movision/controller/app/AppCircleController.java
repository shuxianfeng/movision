package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.boss.CircleFacade;
import com.movision.facade.index.FacadeCircle;
import com.movision.facade.index.FacadePost;
import com.movision.mybatis.category.entity.Category;
import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.circleCategory.entity.CircleCategory;
import com.movision.mybatis.circleCategory.entity.CircleCategoryVo;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostVo;
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
 * @Date 2017/1/20 15:26
 */
@RestController
@RequestMapping("/app/circle/")
public class AppCircleController {

    @Autowired
    private FacadeCircle facadeCircle;

    @Autowired
    private FacadePost facadePost;

    @Autowired
    private CircleFacade circleFacade;

    @ApiOperation(value = "圈子详情页1", notes = "用户返回圈子详情页上半版数据，圈子首页上半部分内容", response = Response.class)
    @RequestMapping(value = "index1", method = RequestMethod.POST)
    public Response queryCircleIndex1(@ApiParam(value = "圈子id") @RequestParam String circleid,
                                      @ApiParam(value = "用户id（登录状态下不可为空）") @RequestParam(required = false) String userid) {
        Response response = new Response();

        CircleVo circleVo = facadeCircle.queryCircleIndex1(circleid, userid);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(circleVo);
        return response;
    }

    @ApiOperation(value = "圈子详情页2", notes = "用户返回圈子详情页下半版数据，圈子首页下半部分内容的所有帖子（含分页）", response = Response.class)
    @RequestMapping(value = "index2", method = RequestMethod.POST)
    public Response queryCircleIndex2(@ApiParam(value = "圈子id") @RequestParam String circleid,
                                      @ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                      @ApiParam(value = "多少条数据") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<PostVo> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List<PostVo> postlist = facadePost.queryCircleIndex2(pager, circleid);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(postlist);
        response.setData(pager);
        return response;
    }

    @ApiOperation(value = "圈子分类", notes = "用户返回所有圈子（按类别分类），待审核新增输出issupport 0 可支持 1 已支持", response = Response.class)
    @RequestMapping(value = "circlelist", method = RequestMethod.POST)
    public Response queryCircleList(@ApiParam(value = "用户id(用户登录状态下为必填)") @RequestParam(required = false) String userid) {
        Response response = new Response();

        List<CircleCategoryVo> circleCategoryList = facadeCircle.queryCircleCategoryList(userid);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(circleCategoryList);
        return response;
    }

    @ApiOperation(value = "圈子简介/公告接口", notes = "用户点击圈子背景图片进入圈子的简介公告页面", response = Response.class)
    @RequestMapping(value = "circleInfo", method = RequestMethod.POST)
    public Response queryCircleInfo(@ApiParam(value = "圈子id") @RequestParam String circleid,
                                    @ApiParam(value = "用户id(登录时为必填)") @RequestParam(required = false) String userid) {
        Response response = new Response();

        CircleVo circleinfo = facadeCircle.queryCircleInfo(circleid, userid);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(circleinfo);
        return response;
    }

    @ApiOperation(value = "待审的圈子点击支持接口", notes = "用于圈子分类列表中待审核的圈子，用户点击支持他的接口", response = Response.class)
    @RequestMapping(value = "supportCircle", method = RequestMethod.POST)
    public Response supportCircle(@ApiParam(value = "用户id") @RequestParam String userid,
                                  @ApiParam(value = "圈子id") @RequestParam String circleid) {
        Response response = new Response();

        int flag = facadeCircle.supportCircle(userid, circleid);

        if (flag == 0) {
            response.setCode(200);
            response.setMessage("支持成功");
        } else if (flag == 1) {
            response.setCode(300);
            response.setMessage("已支持过该圈子");
        }
        return response;
    }

    @ApiOperation(value = "关注圈子接口", notes = "用于用户关注圈子的接口", response = Response.class)
    @RequestMapping(value = "followCircle", method = RequestMethod.POST)
    public Response followCircle(@ApiParam(value = "用户id") @RequestParam String userid,
                                 @ApiParam(value = "圈子id") @RequestParam String circleid) {
        Response response = new Response();

        int flag = facadeCircle.followCircle(userid, circleid);

        if (flag == 0) {
            response.setCode(200);
            response.setMessage("关注成功");
        } else if (flag == 1) {
            response.setCode(300);
            response.setMessage("已关注该圈子，请刷新重试");
        }
        return response;
    }

    @ApiOperation(value = "取消关注圈子接口", notes = "用于用户取消关注圈子的接口", response = Response.class)
    @RequestMapping(value = "cancelFollowCircle", method = RequestMethod.POST)
    public Response cancelFollowCircle(@ApiParam(value = "用户id") @RequestParam String userid,
                                       @ApiParam(value = "圈子id") @RequestParam String circleid) {
        Response response = new Response();

        facadeCircle.cancelFollowCircle(userid, circleid);

        if (response.getCode() == 200) {
            response.setMessage("取消关注成功");
        } else {
            response.setMessage("取消关注失败");
        }
        return response;
    }

    @ApiOperation(value = "查询圈子", notes = "用于首页发帖查询圈子列表接口", response = Response.class)
    @RequestMapping(value = "queryCircle", method = RequestMethod.POST)
    public Response queryCircleAllList(@ApiParam(value = "类型id") @RequestParam String categoryid,
                                       @ApiParam(value = "用户id") @RequestParam String loginid) {
        Response response = new Response();
        List<CircleVo> list = circleFacade.queryCircleList(loginid, categoryid, 0);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        } else {
            response.setMessage("查询失败");
        }
        response.setData(list);
        return response;
    }

    @ApiOperation(value = "首页查询圈子类型", notes = "用于首页发帖查询圈子类型列表接口", response = Response.class)
    @RequestMapping(value = "queryCategory", method = RequestMethod.POST)
    public Response queryCategoryList(@ApiParam(value = "用户id") @RequestParam String loginid) {
        Response response = new Response();
        List<CircleVo> list = circleFacade.queryCategoryList(loginid);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        } else {
            response.setMessage("查询失败");
        }
        response.setData(list);
        return response;
    }
}
