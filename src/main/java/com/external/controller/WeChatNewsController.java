package com.external.controller;

import com.external.form.WeChatNewsForm;
import com.external.service.WeChatNewsService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.weChat.entity.WeChatNews;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信h5资讯信息controller
 * 
 * @author liyang
 * @date 2016年11月22日
 */
@RestController
@RequestMapping("/rest/weChat/weChatNews/")
public class WeChatNewsController {

    @Autowired
    private WeChatNewsService weChatNewsService;

    @RequestMapping(value = { "site/sel_weChat_news_list" }, method = RequestMethod.GET)
    @ApiOperation(value = "微信h5资讯信息", notes = "微信h5资讯信息", response = Response.class)
    public Response selSiteWeChatNewsList(@ApiParam(value = "资讯分类, 1:banner,2:魅力时尚,3:健康人生,4留情岁月,5:成人性趣") @RequestParam(required = false) String type,
            @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") int pageNo, @ApiParam(value = "每页显示的条数") @RequestParam(required = false, defaultValue = "10") int pageSize)
            throws Exception {
        Response response = new Response();
        Map result = new HashMap();
        Paging<WeChatNewsForm> pager = new Paging<>(pageNo, pageSize);
        List<WeChatNewsForm> contentList = weChatNewsService.selWeChatNewsList("", type, "1", pager);
        pager.result(contentList);
        result.put("contentList", pager);
        pager = new Paging<>(1, 3);
        List<WeChatNewsForm> bannerList = weChatNewsService.selWeChatNewsList("", "1", "1", pager);
        result.put("bannerList", bannerList);
        response.setData(result);
        return response;
    }

    @RequestMapping(value = { "oms/sel_weChat_news_list" }, method = RequestMethod.GET)
    @ApiOperation(value = "微信h5资讯信息", notes = "微信h5资讯信息", response = Response.class)
    public Response selWeChatNewsList(@ApiParam(value = "资讯标题") @RequestParam(required = false) String title,
            @ApiParam(value = "资讯分类, 1:banner,2:魅力时尚,3:健康人生,4留情岁月,5:成人性趣") @RequestParam(required = false) String type,
            @ApiParam(value = "发布状态，0：未发布，1：已发布'") @RequestParam(required = false) String status, @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") int pageNo,
            @ApiParam(value = "每页显示的条数") @RequestParam(required = false, defaultValue = "10") int pageSize) throws Exception {
        Response response = new Response();
        Paging<WeChatNewsForm> pager = new Paging<>(pageNo, pageSize);
        List<WeChatNewsForm> list = weChatNewsService.selWeChatNewsList(title, type, status, pager);
        pager.result(list);
        response.setData(pager);
        return response;
    }

    @RequestMapping(value = "oms/add_weChat_news", method = RequestMethod.POST)
    @ApiOperation(value = "添加微信h5资讯信息", notes = "添加微信h5资讯信息", response = Response.class)
    public Response addWeChatNews(@ModelAttribute WeChatNews news) {
        weChatNewsService.addWeChatNews(news);
        return new Response();
    }

    @RequestMapping(value = "oms/upd_weChat_news", method = RequestMethod.POST)
    @ApiOperation(value = "更新微信h5资讯信息", notes = "更新微信h5资讯信息", response = Response.class)
    public Response updWeChatNews(@ModelAttribute WeChatNews news) {
        weChatNewsService.updateWeChatNews(news);
        return new Response();
    }

    @RequestMapping(value = "oms/del_weChat_news", method = RequestMethod.POST)
    @ApiOperation(value = "删除微信h5资讯信息", notes = "删除微信h5资讯信息", response = Response.class)
    public Response delWeChatNews(@ApiParam(value = "资讯主键id") @RequestParam(required = true) String id) {
        weChatNewsService.deleteWeChatNews(id);
        return new Response();
    }

    @RequestMapping(value = "oms/sel_weChat_news", method = RequestMethod.GET)
    @ApiOperation(value = "查询微信h5资讯信息", notes = "查询微信h5资讯信息", response = Response.class)
    public Response selWeChatNews(@ApiParam(value = "资讯主键id") @RequestParam(required = true) String id) {
        Response response = new Response();
        response.setData(weChatNewsService.selWeChatNews(id));
        return response;
    }

    @RequestMapping(value = "oms/batch_del_weChat_news", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除微信h5资讯信息", notes = "批量删除资讯信息", response = Response.class)
    public Response batchDelWeChatNews(@ApiParam(value = "要删除的资讯id字符串") @RequestParam(required = false) String ids) {
        weChatNewsService.batchDelWeChatNews(ids);
        return new Response();
    }

    @RequestMapping(value = "oms/batch_pub_weChat_news", method = RequestMethod.POST)
    @ApiOperation(value = "批量发布微信h5资讯信息", notes = "批量发布资讯信息", response = Response.class)
    public Response batchPubWeChatNews(@ApiParam(value = "要发布的资讯id字符串") @RequestParam(required = false) String ids) {
        weChatNewsService.batchPubWeChatNews(ids);
        return new Response();
    }

}