package com.movision.controller.app2;

import com.movision.common.Response;
import com.movision.common.util.ShiroUtil;
import com.movision.facade.circle.CircleAppFacade;
import com.movision.facade.index.FacadeDiscover;
import com.movision.facade.user.UserFacade;
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.user.entity.Author;
import com.movision.mybatis.user.entity.UserVo;
import com.movision.utils.IntegerUtil;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/7/17 20:54
 * 用户返回美番2.0发现页所有数据的接口控制器
 */
@RestController
@RequestMapping("/app/discover2/")
public class AppNewDiscoverController {
    @Autowired
    private FacadeDiscover facadeDiscover;

    @Autowired
    private CircleAppFacade circleAppFacade;

    @Autowired
    private UserFacade userFacade;

    @ApiOperation(value = "美番2.0发现页上半部分数据返回接口", notes = "用于返回发现页首页的全版数据", response = Response.class)
    @RequestMapping(value = "index", method = RequestMethod.POST)
    public Response getDiscoverIndex(){
        Response response = new Response();

        Map<String, Object> map = facadeDiscover.queryDiscoverIndexData2Up();
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(map);
        return response;
    }

    @ApiOperation(value = "美番2.0发现页热门圈子接口", notes = "用户返回发现页热门圈子数据（一批三条）", response = Response.class)
    @RequestMapping(value = "hotcircle", method = RequestMethod.POST)
    public Response getHotCircle(@ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                 @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "3") String pageSize,
                                 @ApiParam(value = "用户id（登录状态下一定不能为空，非登录状态下可不传）") @RequestParam(required = false) String userid){
        Response response = new Response();

        Paging<CircleVo> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));//默认第一页三条
        List<CircleVo> circleList = circleAppFacade.queryHotCircle(pager, userid);
        pager.result(circleList);
        if (response.getCode() == 200){
            response.setMessage("查询成功");
        }

        response.setData(pager);
        return response;
    }

    @ApiOperation(value = "发现页--热门作者--更多（发现作者）--推荐作者接口，点击更多跳转到二级页面--作者列表页推荐模块接口", notes = "用于返回‘发现作者’列表页推荐模块数据的接口", response = Response.class)
    @RequestMapping(value = "getRecommendAuthor", method = RequestMethod.POST)
    public Response getRecommendAuthor(@ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                       @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize,
                                       @ApiParam(value = "用户id（登录状态下一定不能为空，非登录状态下可不传）") @RequestParam(required = false) String userid){
        Response response = new Response();

        Paging<Author> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));

        List<Author> authorList = userFacade.getHotAuthor(pager, userid);//传入当前登录用户的userid
        pager.result(authorList);
        if (response.getCode() == 200){
            response.setMessage("查询成功");
        }
        response.setData(pager);
        return response;
    }

    @ApiOperation(value = "发现页--热门作者--更多（发现作者）--兴趣作者接口，点击更多跳转到二级页面--作者列表页兴趣模块接口", notes = "用于返回‘发现作者’列表页兴趣模块数据的接口", response = Response.class)
    @RequestMapping(value = "getInterestAuthor", method = RequestMethod.POST)
    public Response getInterestAuthor(@ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                      @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize,
                                      @ApiParam(value = "用户id（登录状态下一定不能为空，非登录状态下可不传）") @RequestParam String userid){
        Response response = new Response();

        Paging<Author> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));

        List<Author> interestAuthorList = userFacade.getInterestAuthor(pager, userid);//传入当前登录用户的userid
        if (response.getCode() == 200){
            response.setMessage("查询成功");
        }

        response.setData(interestAuthorList);
        return response;
    }

    @ApiOperation(value = "发现页--热门作者--更多（发现作者）--附近作者接口，点击更多跳转到二级页面--作者列表页附近模块接口", notes = "用于返回‘发现作者’列表页附近模块数据的接口", response = Response.class)
    @RequestMapping(value = "getNearAuthor", method = RequestMethod.POST)
    public Response getNearAuthor(@ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                  @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize,
                                  @ApiParam(value = "经度（如118.7935942346943）") @RequestParam(required = false) String lng,
                                  @ApiParam(value = "纬度（如32.05606138741064）") @RequestParam(required = false) String lat,
                                  @ApiParam(value = "用户id（登录状态下一定不能为空，非登录状态下可不传）") @RequestParam(required = false) String userid){
        Response response = new Response();

        Paging<Author> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));

        List<Author> nearAuthorList = userFacade.getNearAuthor(pager, lng, lat, userid);//传入当前登录用户的userid
        if (response.getCode() == 200){
            response.setMessage("查询成功");
        }

        response.setData(nearAuthorList);
        return response;
    }

    @ApiOperation(value = "热门排行帖子-达人评论-总排行", notes = "热门排行帖子-达人评论-总排行", response = Response.class)
    @RequestMapping(value = "search_hot_comment_post_in_all", method = RequestMethod.GET)
    public Response searchHotCommentPostInAll(@ApiParam @RequestParam(required = false, defaultValue = "1") String pageNo,
                                              @ApiParam @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();


        return response;
    }
}