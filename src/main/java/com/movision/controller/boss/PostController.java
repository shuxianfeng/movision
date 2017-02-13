package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.boss.PostFacade;
import com.movision.mybatis.comment.entity.Comment;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostList;
import com.movision.mybatis.rewarded.entity.RewardedVo;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.entity.Validateinfo;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/2/7 9:05
 */
@RestController
@RequestMapping("/boss/post")
public class PostController {
    @Autowired
    PostFacade postFacade;

    /**
     * 后台管理-查询帖子列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询帖子列表", notes = "查询帖子列表", response = Response.class)
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Response queryPostByList(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                    @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<PostList> pager = new Paging<PostList>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<PostList> list = postFacade.queryPostByList(pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 查询发帖人信息
     *
     * @param postid
     * @return
     */
    @ApiOperation(value = "查询发帖人信息", notes = "查询发帖人信息", response = Response.class)
    @RequestMapping(value = "/query_posted_man", method = RequestMethod.POST)
    public Response queryPostByPosted(@ApiParam(value = "帖子id") @RequestParam String postid) {
        Response response = new Response();
        User user = postFacade.queryPostByPosted(postid);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(user);
        return response;
    }

    /**
     * 删除帖子
     *
     * @param postid
     * @return
     */
    @ApiOperation(value = "逻辑删除帖子", notes = "逻辑删除帖子", response = Response.class)
    @RequestMapping(value = "/delete_post", method = RequestMethod.POST)
    public Response deletePost(@ApiParam(value = "帖子id") @RequestParam String postid) {
        Response response = new Response();
        Map<String, Integer> map = postFacade.deletePost(postid);
        if (response.getCode() == 200) {
            response.setMessage("删除成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 后台管理-帖子列表-查看评论
     *
     * @param postid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查看帖子评论", notes = "查看帖子评论", response = Response.class)
    @RequestMapping(value = "/query_post_appraise", method = RequestMethod.POST)
    public Response queryPostAppraise(@ApiParam(value = "帖子id") @RequestParam String postid,
                                      @RequestParam(required = false, defaultValue = "1") String pageNo,
                                      @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<CommentVo> pager = new Paging<CommentVo>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<CommentVo> list = postFacade.queryPostAppraise(postid, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 后台管理-帖子列表-查看评论-评论详情列表
     *
     * @param commentid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询帖子评论详情", notes = "用于查询帖子评论详情接口", response = Response.class)
    @RequestMapping(value = "/query_post_comment_particulars", method = RequestMethod.POST)
    public Response queryPostByCommentParticulars(@ApiParam(value = "评论id") @RequestParam String commentid,
                                                  @RequestParam(required = false, defaultValue = "1") String pageNo,
                                                  @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<CommentVo> pager = new Paging<CommentVo>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<CommentVo> list = postFacade.queryPostByCommentParticulars(commentid, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 后台管理-帖子列表-查看评论-删除帖子评论
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除帖子评论", notes = "删除帖子评论", response = Response.class)
    @RequestMapping(value = "/delete_post_appraise", method = RequestMethod.POST)
    public Response deletePostAppraise(@ApiParam(value = "评论id") @RequestParam String id) {
        Response response = new Response();
        postFacade.deletePostAppraise(id);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        return response;
    }

    /**
     * 后台管理-帖子列表-帖子打赏
     *
     * @param postid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查看帖子打赏列表", notes = "查看帖子打赏列表", response = Response.class)
    @RequestMapping(value = "/query_post_award", method = RequestMethod.POST)
    public Response queryPostAward(@ApiParam(value = "帖子id") @RequestParam String postid,
                                   @RequestParam(required = false, defaultValue = "1") String pageNo,
                                   @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<RewardedVo> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<RewardedVo> list = postFacade.queryPostAward(postid, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 后台管理-帖子列表-帖子预览
     *
     * @param postid
     * @return
     */
    @ApiOperation(value = "帖子预览", notes = "帖子预览", response = Response.class)
    @RequestMapping(value = "/query_post_particulars", method = RequestMethod.POST)
    public Response queryPostParticulars(@ApiParam(value = "帖子id") @RequestParam String postid) {
        Response response = new Response();
        PostList list = postFacade.queryPostParticulars(postid);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(list);
        return response;
    }


    /**
     * 后台管理-添加帖子
     *
     * @param title
     * @param subtitle
     * @param type
     * @param circleid
     * @param coverimg
     * @param postcontent
     * @param isessence
     * @param time
     * @return
     */
    @ApiOperation(value = "添加帖子", notes = "添加帖子", response = Response.class)
    @RequestMapping(value = "/add_post", method = RequestMethod.POST)
    public Response addPost(@ApiParam(value = "帖子标题") @RequestParam String title,//帖子标题
                            @ApiParam(value = "帖子副标题") @RequestParam String subtitle,//帖子副标题
                            @ApiParam(value = "帖子类型") @RequestParam String type,//帖子类型
                            @ApiParam(value = "圈子id") @RequestParam String circleid,//圈子id
                            @ApiParam(value = "帖子封面") @RequestParam(required = false) String coverimg,//帖子封面
                            @ApiParam(value = "视频地址") @RequestParam(required = false) String vid,//视频url
                            @ApiParam(value = "帖子内容") @RequestParam String postcontent,//帖子内容
                            @ApiParam(value = "首页精选") @RequestParam(required = false) String isessence,//首页精选
                            @ApiParam(value = "圈子精选") @RequestParam(required = false) String isessencepool,//精选池中的帖子圈子精选贴
                            @ApiParam(value = "精选日期") @RequestParam(required = false) String time) {//精选日期
        Response response = new Response();
        Map<String, Integer> resaut = postFacade.addPost(title, subtitle, type, circleid, vid, coverimg, postcontent, isessence, isessencepool, time);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(resaut);
        return response;
    }


    /**
     * 后台管理-帖子列表-帖子加精
     *
     * @param postid
     * @return
     */
    @ApiOperation(value = "帖子加精", notes = "用于帖子加精接口", response = Response.class)
    @RequestMapping(value = "/add_post_choiceness", method = RequestMethod.POST)
    public Response addPostChoiceness(@ApiParam(value = "帖子id") @RequestParam String postid,
                                      @ApiParam(value = "精选日期") @RequestParam String essencedate,
                                      @ApiParam(value = "精选排序") @RequestParam String orderid) {
        Response response = new Response();
        Map<String, Integer> result = postFacade.addPostChoiceness(postid, essencedate, orderid);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        response.setData(result);
        return response;
    }


/*
    *//**
     * 帖子按条件查询
     * @param title
     * @param circleid
     * @param name
     * @param date
     * @return
     *//*
    @ApiOperation(value = "帖子搜索",notes = "帖子搜索",response = Response.class)
    @RequestMapping(value = "/post_search",method = RequestMethod.POST)
    public Response postSearch(@RequestParam(required = false) String pageNo,
                               @RequestParam(required = false) String pageSize,
                               @ApiParam(value = "帖子标题")@RequestParam(required = false) String title,
                               @ApiParam(value = "圈子id")@RequestParam(required = false) String circleid,
                               @ApiParam(value = "发帖人")@RequestParam(required = false) String name,
                               @ApiParam(value = "精选日期")@RequestParam(required = false) Date date){
        Response response=new Response();
        List<Object> list=postFacade.postSearch(title,circleid,name,date,pageNo,pageSize);
        if (response.getCode()==200){
            response.setMessage("查询成功");
        }
        response.setData(list);
        return response;
    }*/


}
