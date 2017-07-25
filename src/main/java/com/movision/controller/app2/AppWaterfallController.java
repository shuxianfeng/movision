package com.movision.controller.app2;

import com.movision.common.Response;
import com.movision.facade.index.FacadePost;
import com.movision.facade.msgCenter.MsgCenterFacade;
import com.movision.facade.user.UserFacade;
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.post.entity.PostVo;
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
 * @Author zhanglei
 * @Date 2017/7/17 20:32
 */
@RestController
@RequestMapping("/app/waterfall/")
public class AppWaterfallController {

    @Autowired
    private FacadePost facadePost;

    @Autowired
    private MsgCenterFacade msgCenterFacade;
    @Autowired
    private UserFacade userFacade;

    /**
     * 下拉刷新
     *
     * @return
     */
    @ApiOperation(value = "下拉刷新", notes = "下拉刷新", response = Response.class)
    @RequestMapping(value = "userRefreshListNew", method = RequestMethod.POST)
    public Response userRefreshList(@ApiParam(value = "用户id ") @RequestParam(required = false) String userid,
                                    @ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                    @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize,
                                    @ApiParam(value = "类型 1：推荐2：关注3：本地 4：圈子 5：标签") @RequestParam(required = false) int type,
                                    @ApiParam(value = "地区") @RequestParam(required = false) String area,
                                    @ApiParam(value = "圈子id") @RequestParam(required = false) String circleid,
                                    @ApiParam(value = "标签id") @RequestParam(required = false) String labelid) {
        Response response = new Response();
        Paging<PostVo> pager = new Paging<PostVo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List map = facadePost.userRefreshListNew(userid, pager, type, area, circleid, labelid);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(map);
        return response;

    }

    /**
     * 首页滑动列表
     *
     * @return
     */
    @ApiOperation(value = "首页滑动列表", notes = "首页滑动列表", response = Response.class)
    @RequestMapping(value = "indexHomeList", method = RequestMethod.POST)
    public Response indexHomeList() {
        Response response = new Response();
        List<CircleVo> list = facadePost.indexHomeList();
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(list);
        return response;
    }

    /**
     * 用户刷新的历史列表
     *
     * @param userid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "用户刷新的历史记录列表", notes = "用户刷新的历史记录列表", response = Response.class)
    @RequestMapping(value = "userReflushHishtoryRecord", method = RequestMethod.POST)
    public Response userReflushHishtoryRecord(@ApiParam(value = "用户id") @RequestParam(required = false) String userid,
                                              @ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                              @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<PostVo> pager = new Paging<PostVo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List map = facadePost.userReflushHishtoryRecord(userid, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(map);
        return response;
    }


    /**
     * 所有未读消息
     * @param userid
     * @return
     */
     @ApiOperation(value = "所有未读消息", notes = "所有未读消息", response = Response.class)
     @RequestMapping(value = "queryUserAllUnreadMessage", method = RequestMethod.POST)
     public Response queryUserAllUnreadMessage(@ApiParam(value = "用户id") @RequestParam(required = false) String userid){
     Response response = new Response();
     Integer count =msgCenterFacade.queryUserAllUnreadMessage(userid);
     if(response.getCode()==200){
     response.setMessage("返回成功");
     }
     response.setData(count);
         return response;
     }

    /**
     * @return
     */
    @ApiOperation(value = "个人主页上半部分", notes = "个人主页上半部分", response = Response.class)
    @RequestMapping(value = "queryPersonalHomepage", method = RequestMethod.POST)
    public Response queryPersonalHomepage(@ApiParam(value = "用户id") @RequestParam(required = false) String userid) {
        Response response = new Response();
        UserVo list = userFacade.queryPersonalHomepage(userid);
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        response.setData(list);
        return response;
     }

    /**
     * @return
     */
    @ApiOperation(value = "个人主页下半部分", notes = "个人主页下半部分", response = Response.class)
    @RequestMapping(value = "mineBottle", method = RequestMethod.POST)
    public Response mineBottle(@ApiParam(value = "类型 1 帖子 2 活动 3 收藏（必填）") @RequestParam int type,
                               @ApiParam(value = "用户id（必填，被查看的这个人的userid，被被被！！！）") @RequestParam String userid,
                               @ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                               @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<PostVo> pager = new Paging<PostVo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List list = userFacade.mineBottle(type, userid, pager);
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        response.setData(list);
        return response;
    }
}
