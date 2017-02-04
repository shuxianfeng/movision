package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.index.FacadePost;
import com.movision.mybatis.accusation.service.AccusationService;
import com.movision.mybatis.post.entity.ActiveVo;
import com.movision.mybatis.post.entity.PostVo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/1/19 15:42
 */
@RestController
@RequestMapping("/app/post/")
public class AppPostController {

    @Autowired
    private FacadePost facadePost;

    @ApiOperation(value = "帖子详情数据返回接口", notes = "用于返回请求帖子详情内容", response = Response.class)
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    public Response queryPostDetail(@ApiParam(value = "帖子id") @RequestParam String postid,
                                    @ApiParam(value = "用户id(登录状态下不可为空)") @RequestParam(required = false) String userid,
                                    @ApiParam(value = "帖子类型：0 普通帖 1 原生视频帖( isactive为0时该字段不为空)") @RequestParam String type) {
        Response response = new Response();

        PostVo post = facadePost.queryPostDetail(postid, userid, type);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(post);
        return response;
    }

    @ApiOperation(value = "活动详情数据返回接口", notes = "用于返回请求活动详情内容", response = Response.class)
    @RequestMapping(value = "activeDetail", method = RequestMethod.POST)
    public Response queryActiveDetail(@ApiParam(value = "活动id") @RequestParam String postid,
                                      @ApiParam(value = "活动类型:0 告知类活动 1 商城促销类活动") @RequestParam String activetype,
                                      @ApiParam(value = "第几页（为商城促销类）") @RequestParam(required = false) String pageNo,
                                      @ApiParam(value = "每页条数") @RequestParam(required = false) String pageSize) {

        Response response = new Response();

        ActiveVo active = facadePost.queryActiveDetail(postid, activetype, pageNo, pageSize);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(active);
        return response;
    }

    @ApiOperation(value = "查询往期3天的精选帖子列表", notes = "用于返回往期3天的精选帖子列表的接口", response = Response.class)
    @RequestMapping(value = "pastPost", method = RequestMethod.POST)
    public Response queryPastPostDetail(@ApiParam(value = "查询日期") @RequestParam(required = false) String date) {
        Response response = new Response();


        Map<String, Object> postmap = facadePost.queryPastPostDetail(date);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(postmap);
        return response;
    }


    @ApiOperation(value = "APP端发布普通帖子", notes = "用于APP端发布普通帖子的接口", response = Response.class)
    @RequestMapping(value = "releasePost", method = RequestMethod.POST)
    public Response releasePost(HttpServletRequest request, @ApiParam(value = "用户id") @RequestParam String userid,
                                @ApiParam(value = "用户等级(根据该字段判断是否为大V)") @RequestParam String level,
                                @ApiParam(value = "所属圈子id") @RequestParam String circleid,
                                @ApiParam(value = "帖子主标题(限18个字以内)") @RequestParam String title,
                                @ApiParam(value = "帖子内容") @RequestParam String postcontent,
                                @ApiParam(value = "是否为活动：0 帖子 1 活动") @RequestParam String isactive,
                                @ApiParam(value = "上传的帖子封面图片截图") @RequestParam("file") MultipartFile file) {
        Response response = new Response();

        int count = facadePost.releasePost(request, userid, level, circleid, title, postcontent, isactive, file);

        if (count == 1) {
            response.setCode(200);
            response.setMessage("发布成功");
        } else if (count == 0) {
            response.setCode(300);
            response.setMessage("系统异常，APP普通帖发布失败");
        } else if (count == -1) {
            response.setCode(201);
            response.setMessage("用户不具备发帖权限");
        }
        return response;
    }


    /**
     * 帖子点赞接口
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "更新帖子点赞次数", notes = "用于帖子点赞接口", response = Response.class)
    @RequestMapping(value = "updateZan", method = RequestMethod.POST)
    public Response updatePostByZanSum(@ApiParam(value = "帖子id") @RequestParam String id) {
        Response response = new Response();
        int sum = facadePost.updatePostByZanSum(id);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(sum);
        return response;
    }

    /**
     * 帖子举报接口
     *
     * @param userid
     * @param postid
     * @return
     */
    @ApiOperation(value = "帖子举报", notes = "用于举报帖子接口", response = Response.class)
    @RequestMapping(value = "inAccusation", method = RequestMethod.POST)
    public Response insertPostByAccusation(@ApiParam(value = "用户id") @RequestParam String userid,
                                           @ApiParam(value = "帖子id") @RequestParam String postid) {
        Response response = new Response();
        int type = facadePost.insertPostByAccusation(userid, postid);

        if (response.getCode() == 200 || type == 200) {
            response.setMessage("操作成功");
        }
        return response;
    }

    /**
     * 发现页热门活动————查看全部活动接口
     *
     * @param pageNo
     * @param pageSize
     * @return Response
     */
    @ApiOperation(value = "查看全部活动", notes = "用于返回发现页热门活动————查看全部活动接口(enddays为-1活动还未开始 为0活动已结束 为其他时为距离结束的剩余天数)", response = Response.class)
    @RequestMapping(value = "queryAllActive", method = RequestMethod.POST)
    public Response queryAllActive(@ApiParam(value = "第几页") @RequestParam(required = false) String pageNo,
                                   @ApiParam(value = "每页多少条") @RequestParam(required = false) String pageSize) {
        Response response = new Response();
        List<PostVo> activeList = facadePost.queryAllActive(pageNo, pageSize);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(activeList);

        return response;
    }

    /**
     * 告知类活动————点击参与活动接口
     *
     * @param postid
     * @param userid
     * @return Response
     */
    @ApiOperation(value = "点击参与活动接口（告知类）", notes = "用于告知类活动点击参与活动", response = Response.class)
    @RequestMapping(value = "partActive", method = RequestMethod.POST)
    public Response partActive(@ApiParam(value = "活动id") @RequestParam String postid,
                               @ApiParam(value = "参与用户id") @RequestParam String userid) {
        Response response = new Response();
        int flag = facadePost.partActive(postid, userid);

        if (flag == 1) {
            response.setCode(200);
            response.setMessage("活动参与成功");
        } else if (flag == -1) {
            response.setCode(201);
            response.setMessage("已参与过该活动");
        }

        return response;
    }
}
