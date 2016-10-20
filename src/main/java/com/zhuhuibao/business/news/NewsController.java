package com.zhuhuibao.business.news;

import java.util.*;

import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.news.form.NewsForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.news.entity.News;
import com.zhuhuibao.service.NewsService;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * 资讯相关业务控制层
 *
 * @author liyang
 * @date 2016年10月17日
 */
@RestController
@RequestMapping("/rest/news/oms/")
@Api(value = "news", description = "资讯")
public class NewsController {

    private static final Logger log = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    private NewsService newsService;

    @RequestMapping(value = "/sel_news_list", method = RequestMethod.GET)
    @ApiOperation(value = "分页查询资讯相关信息", notes = "分页查询资讯相关信息", response = Response.class)
    public Response sel_news_list(@ApiParam(value = "标题") @RequestParam(required = false) String title, @ApiParam(value = "分类") @RequestParam(required = false) String type,
            @ApiParam(value = "状态") @RequestParam(required = false) String status, @ApiParam(value = "页码") @RequestParam(required = false) int pageNo,
            @ApiParam(value = "每页显示的数目") @RequestParam(required = false) int pageSize) {
        Response response = new Response();
        Paging<NewsForm> pager = new Paging<>(pageNo, pageSize);
        try {
            List<NewsForm> list = newsService.sel_news_list(title, type, status, pager);
            pager.result(list);
            response.setData(pager);
        } catch (Exception e) {
            log.error("sel_news_list error! ", e);
            e.printStackTrace();
        }
        return response;
    }

    @RequestMapping(value = "/add_news", method = RequestMethod.POST)
    @ApiOperation(value = "添加资讯信息", notes = "添加资讯信息", response = Response.class)
    public Response add_news(@ApiParam(value = "资讯一级分类") @RequestParam String type, @ApiParam(value = "资讯二级分类") @RequestParam(required = false) String subType,
            @ApiParam(value = "缩略图") @RequestParam(required = false) String photo, @ApiParam(value = "资讯标题") @RequestParam(required = false) String title,
            @ApiParam(value = "简短标题") @RequestParam(required = false) String shortTitle, @ApiParam(value = "跳转路径") @RequestParam(required = false) String jumpUrl,
            @ApiParam(value = "来源") @RequestParam(required = false) String source, @ApiParam(value = "关键字") @RequestParam(required = false) String keywords,
            @ApiParam(value = "文章描述：导读") @RequestParam(required = false) String introduction, @ApiParam(value = "热度/点击率") @RequestParam(required = false) long views,
            @ApiParam(value = "发布时间") @RequestParam(required = false) String publishTime, @ApiParam(value = "附件名称") @RequestParam(required = false) String attachName,
            @ApiParam(value = "附件路径") @RequestParam(required = false) String attachUrl, @ApiParam(value = "资讯内容") @RequestParam String content,
            @ApiParam(value = "状态") @RequestParam(required = false) int status, @ApiParam(value = "推荐显示位置") @RequestParam(required = false) String recPlace) {
        News news = getNews(type, subType, photo, title, shortTitle, jumpUrl, source, keywords, introduction, views, publishTime, attachName, attachUrl, content, status);
        newsService.add_news(news, recPlace);
        return new Response();
    }

    @RequestMapping(value = "/upd_news", method = RequestMethod.POST)
    @ApiOperation(value = "更新资讯信息", notes = "更新资讯信息", response = Response.class)
    public Response update_news(@ApiParam(value = "主键id") @RequestParam(required = true) String id, @ApiParam(value = "资讯一级分类") @RequestParam(required = false) String type,
            @ApiParam(value = "资讯二级分类") @RequestParam(required = false) String subType, @ApiParam(value = "缩略图") @RequestParam(required = false) String photo,
            @ApiParam(value = "资讯标题") @RequestParam(required = false) String title, @ApiParam(value = "简短标题") @RequestParam(required = false) String shortTitle,
            @ApiParam(value = "跳转路径") @RequestParam(required = false) String jumpUrl, @ApiParam(value = "来源") @RequestParam(required = false) String source,
            @ApiParam(value = "关键字") @RequestParam(required = false) String keywords, @ApiParam(value = "文章描述：导读") @RequestParam(required = false) String introduction,
            @ApiParam(value = "热度/点击率") @RequestParam(required = false) long views, @ApiParam(value = "发布时间") @RequestParam(required = false) String publishTime,
            @ApiParam(value = "附件名称") @RequestParam(required = false) String attachName, @ApiParam(value = "附件路径") @RequestParam(required = false) String attachUrl,
            @ApiParam(value = "资讯内容") @RequestParam(required = false) String content, @ApiParam(value = "状态") @RequestParam(required = false) int status,
            @ApiParam(value = "推荐显示位置") @RequestParam(required = false) String recPlace) {
        News news = getNews(type, subType, photo, title, shortTitle, jumpUrl, source, keywords, introduction, views, publishTime, attachName, attachUrl, content, status);
        news.setId(Long.valueOf(id));
        newsService.update_news(news, recPlace);
        return new Response();
    }

    @RequestMapping(value = "/del_news", method = RequestMethod.POST)
    @ApiOperation(value = "删除资讯信息", notes = "删除资讯信息", response = Response.class)
    public Response del_news(@ApiParam(value = "资讯主键id") @RequestParam(required = false) int id) {
        newsService.del_news(id);
        return new Response();
    }

    @RequestMapping(value = "/sel_news", method = RequestMethod.GET)
    @ApiOperation(value = "查询资讯信息", notes = "查询资讯信息", response = Response.class)
    public Response sel_news(@ApiParam(value = "资讯主键id") @RequestParam(required = false) int id) {
        Response response = new Response();
        response.setData(newsService.sel_news(id));
        return response;
    }

    @RequestMapping(value = "/batch_del_news", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除资讯信息", notes = "批量删除资讯信息", response = Response.class)
    public Response batch_del_news(@ApiParam(value = "要删除的资讯id字符串") @RequestParam(required = false) String ids) {
        newsService.batch_del_news(ids);
        return new Response();
    }

    @RequestMapping(value = "/batch_pub_news", method = RequestMethod.POST)
    @ApiOperation(value = "批量发布资讯信息", notes = "批量发布资讯信息", response = Response.class)
    public Response batch_pub_news(@ApiParam(value = "要发布的资讯id字符串") @RequestParam(required = false) String ids) {
        newsService.batch_pub_news(ids);
        return new Response();
    }

    @RequestMapping(value = "/batch_update_rec_place", method = RequestMethod.POST)
    @ApiOperation(value = "批量修改资讯信息推荐位置", notes = "批量修改资讯信息", response = Response.class)
    public Response batch_modifty_rec_place(@ApiParam(value = "要发布的资讯id字符串") @RequestParam(required = false) String ids,
            @ApiParam(value = "要增加(减少)属性字符串") @RequestParam(required = false) String recPlace, @ApiParam(value = "增加(减少)") @RequestParam(required = false) int type) {
        newsService.batch_modifty_rec_place(ids, recPlace, type);
        return new Response();
    }

    /**
     * 封装news对象
     * 
     * @return
     */
    private News getNews(String type, String subType, String photo, String title, String shortTitle, String jumpUrl, String source, String keywords, String introduction, long views,
            String publishTime, String attachName, String attachUrl, String content, int status) {
        News news = new News();
        news.setAddTime(new Date());
        news.setAttachName(attachName);
        news.setAttachUrl(attachUrl);
        news.setContent(content);
        news.setIntroduction(introduction);
        news.setJumpUrl(jumpUrl);
        news.setKeywords(keywords);
        news.setShortTitle(shortTitle);
        news.setType(Integer.valueOf(type) + 1);
        news.setSubType(Integer.valueOf(subType) + 1);
        news.setTitle(title);
        news.setSource(source);
        news.setViews(views);
        news.setStatus(status);
        news.setPublishTime(DateUtils.str2Date(publishTime, "yyyy-MM-dd HH:mm:ss"));
        news.setPhoto(photo);
        news.setPublisherId(ShiroUtil.getOmsCreateID());
        news.setUpdateTime(new Date());
        return news;
    }
}
