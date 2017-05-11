package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.user.UserFacade;
import com.movision.mybatis.post.entity.ActiveVo;
import com.movision.mybatis.post.entity.Post;
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
 * @Author shuxf
 * @Date 2017/2/6 17:07
 * 个人中心
 */
@RestController
@RequestMapping("/app/user/")
public class AppUserController {

    @Autowired
    private UserFacade userFacade;

    @ApiOperation(value = "个人主页--个人信息", notes = "用于返回个人主页中个人信息的接口", response = Response.class)
    @RequestMapping(value = "personPage", method = RequestMethod.POST)
    public Response personPage(@ApiParam(value = "用户id") @RequestParam String userid) {
        Response response = new Response();

        UserVo userVo = userFacade.queryUserInfo(userid);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(userVo);
        return response;
    }

    @ApiOperation(value = "个人主页--帖子（发布的帖子和分享的帖子）", notes = "用于返回个人主页中帖子页签中个人发布的历史帖子和分享过的历史帖子", response = Response.class)
    @RequestMapping(value = "personPost", method = RequestMethod.POST)
    public Response personPost(@ApiParam(value = "用户id") @RequestParam String userid,
                               @ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                               @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();

        Paging<PostVo> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List<PostVo> personPostList = userFacade.personPost(pager, userid);
        pager.result(personPostList);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(pager);
        return response;
    }

    @ApiOperation(value = "个人主页--活动（用户曾经参与过的活动）", notes = "用于返回个人主页中活动页签中个人曾经参与过的历史活动记录", response = Response.class)
    @RequestMapping(value = "personActive", method = RequestMethod.POST)
    public Response personActive(@ApiParam(value = "用户id") @RequestParam String userid,
                                 @ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                 @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();

        Paging<ActiveVo> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List<ActiveVo> personActiveList = userFacade.personActive(pager, userid);
        pager.result(personActiveList);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(pager);
        return response;
    }

    @ApiOperation(value = "用户在应用商店评价成功后调用该接口奖励积分", notes = "用户在应用商店评价成功后调用该接口奖励积分", response = Response.class)
    @RequestMapping(value = "commetAPP", method = RequestMethod.POST)
    public Response commetAPP() {
        Response response = new Response();

        userFacade.commetAPP();

        if (response.getCode() == 200) {
            response.setMessage("调用成功");
        }
        return response;
    }

    @ApiOperation(value = "用户在分享成功后调用该接口通知服务端", notes = "用户在分享成功后调用该接口通知服务端（包含个人主页、帖子、活动、商品）", response = Response.class)
    @RequestMapping(value = "shareSucNotice", method = RequestMethod.POST)
    public Response shareSucNotice(@ApiParam(value = "分享类型: 0 帖子/活动 1 商品 2 个人主页") @RequestParam String type,
                                   @ApiParam(value = "用户id") @RequestParam(required = false) String userid,
                                   @ApiParam(value = "分享渠道(0 QQ 1 QQ空间 2 微信 3 朋友圈 4 新浪微博 )") @RequestParam String channel,
                                   @ApiParam(value = "帖子或活动id（type为0时不为空）") @RequestParam(required = false) String postid,
                                   @ApiParam(value = "商品id（type为1时不为空）") @RequestParam(required = false) String goodsid,
                                   @ApiParam(value = "被分享的用户id（type为2时不为空）") @RequestParam(required = false) String beshareuserid) {
        Response response = new Response();

        userFacade.shareSucNotice(type, userid, channel, postid, goodsid, beshareuserid);

        if (response.getCode() == 200) {
            response.setMessage("调用成功");
        }
        return response;
    }
}
