package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.common.util.ShiroUtil;
import com.movision.facade.index.FacadeDiscover;
import com.movision.facade.index.FacadeIndex;
import com.movision.fsearch.pojo.spec.NormalSearchSpec;
import com.movision.fsearch.service.exception.ServiceException;
import com.movision.fsearch.service.impl.PostSearchService;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.postLabel.entity.PostLabel;
import com.movision.mybatis.user.entity.User;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/1/17 15:25
 */
@RestController
@RequestMapping("/app/index/")
public class AppIndexController {

    private static Logger log = LoggerFactory.getLogger(AppIndexController.class);

    @Autowired
    private FacadeIndex facadeIndex;

    @Autowired
    private PostSearchService postSearchService;

    @Autowired
    private FacadeDiscover facadeDiscover;

    @ApiOperation(value = "首页数据返回接口", notes = "用户返回首页整版数据(活动贴的话 enddays为-1活动还未开始 为0活动已结束 为其他时为距离结束的剩余天数)", response = Response.class)
    @RequestMapping(value = "index", method = RequestMethod.POST)
    public Response queryIndexData(@ApiParam(value = "用户id") @RequestParam(required = false) String userid) {
        Response response = new Response();

        Map<String, Object> map = facadeIndex.queryIndexData(userid);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(map);
        return response;
    }

    @RequestMapping(value = {"search_post"}, method = RequestMethod.GET)
    @ApiOperation(value = "查看更多-搜索帖子", notes = "查看更多-搜索帖子", response = Response.class)
    public Response search_post_and_activity(@ApiParam @ModelAttribute NormalSearchSpec spec) throws IOException {

        if (spec.getLimit() <= 0 || spec.getLimit() > 100) {
            spec.setLimit(10);
        }
        Response response = new Response();
        response.setCode(200);
        Map<String, Object> ret;
        try {
            log.debug("测试搜索词汉字是否乱码>>>>>>"+spec.getQ());
            ret = postSearchService.search(spec);
            response.setMsgCode(1);
            response.setMessage("OK!");
            response.setData(ret);
        } catch (Exception e) {
            response.setMsgCode(0);
            response.setMessage("search error!");
            log.error("searchProducts error >>>", e);
        }

        return response;
    }

    @RequestMapping(value = {"search_all"}, method = RequestMethod.GET)
    @ApiOperation(value = "搜索全部", notes = "搜索全部", response = Response.class)
    public Response searchAll(@ApiParam @ModelAttribute NormalSearchSpec spec) throws IOException, ServiceException, InvalidKeyException, NoSuchAlgorithmException {
        Response response = new Response();
        spec.setQ(StringUtil.emptyToNull(spec.getQ()));
        if (spec.getQ() == null) {
            response.setCode(400);
            response.setMessage("请输入搜索词");
            log.warn("搜索词为空！");
        } else {
            response.setMessage("搜索成功！");
            response.setData(facadeDiscover.searchAll(spec));
        }

        return response;
    }

    @RequestMapping(value = {"search_circle"}, method = RequestMethod.GET)
    @ApiOperation(value = "查看更多-搜索圈子", notes = "查看更多-搜索圈子", response = Response.class)
    public Response searchCircle(@ApiParam @RequestParam String name,
                                 @ApiParam(value = "当前页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                 @ApiParam(value = "当前页") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<Circle> pager = new Paging<Circle>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List<Circle> circleList = facadeDiscover.findAllCircleByNameInSearch(pager, name);
        pager.result(circleList);
        response.setData(pager);
        return response;
    }

    @RequestMapping(value = {"search_user"}, method = RequestMethod.GET)
    @ApiOperation(value = "查看更多-搜索作者", notes = "查看更多-搜索作者", response = Response.class)
    public Response searchUser(@ApiParam @RequestParam String name,
                               @ApiParam(value = "当前页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                               @ApiParam(value = "当前页") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<User> pager = new Paging<User>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List<User> userList = facadeDiscover.findAllUserByName(pager, name);
        pager.result(userList);
        response.setData(pager);
        return response;
    }

    @RequestMapping(value = {"search_label"}, method = RequestMethod.GET)
    @ApiOperation(value = "查看更多-搜索标签", notes = "查看更多-搜索标签", response = Response.class)
    public Response searchLabel(@ApiParam @RequestParam String name,
                                @ApiParam(value = "当前页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                @ApiParam(value = "当前页") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<PostLabel> pager = new Paging<PostLabel>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List<PostLabel> postLabelList = facadeDiscover.findAllLabelByName(pager, name);
        pager.result(postLabelList);
        response.setData(pager);
        return response;
    }


    @RequestMapping(value = {"get_post_hot_search_word_and_history"}, method = RequestMethod.GET)
    @ApiOperation(value = "查询帖子热门搜索词和搜索历史记录", notes = "查询热门搜索词和搜索历史记录", response = Response.class)
    public Response getHotSearchWordAndHistory() {

        Response response = new Response();
        response.setData(postSearchService.getHotwordAndHistory());
        return response;
    }

    @RequestMapping(value = {"get_userlookhistory"}, method = RequestMethod.GET)
    @ApiOperation(value = "查询用户浏览历史", notes = "查询用户浏览历史", response = Response.class)
    public Response getUserLookingHistory(@RequestParam(required = false, defaultValue = "1") int page,
                                          @RequestParam(required = false, defaultValue = "10") int pageSize) {

        Response response = new Response();
        response.setData(postSearchService.getUserLookingHistory(page,pageSize));
        return response;
    }


    @RequestMapping(value = {"get_post_hot_search_word_and_history_isdel"}, method = RequestMethod.GET)
    @ApiOperation(value = "清除搜索记录", notes = "清除搜索记录", response = Response.class)
    public Response UpdateSearchIsdel() {
        Response response = new Response();
        postSearchService.UpdateSearchIsdel(ShiroUtil.getAppUserID());
        return response;
    }

}
