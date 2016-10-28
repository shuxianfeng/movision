package com.zhuhuibao.business.news;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.news.entity.News;
import com.zhuhuibao.mybatis.news.form.NewsForm;
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
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(DateUtils.DEFAULT_DATE_FORMAT, true));
    }

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
            List<NewsForm> list = newsService.selNewsList(title, type, status, pager);
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
    public Response add_news(@ModelAttribute News news, @ApiParam(value = "推荐显示位置") @RequestParam(required = false) String recPlace) {
        newsService.addNews(news, recPlace);
        return new Response();
    }

    @RequestMapping(value = "/upd_news", method = RequestMethod.POST)
    @ApiOperation(value = "更新资讯信息", notes = "更新资讯信息", response = Response.class)
    public Response update_news(@ModelAttribute News news, @ApiParam(value = "推荐显示位置") @RequestParam(required = false) String recPlace) {
        newsService.updateNews(news, recPlace);
        return new Response();
    }

    @RequestMapping(value = "/del_news", method = RequestMethod.POST)
    @ApiOperation(value = "删除资讯信息", notes = "删除资讯信息", response = Response.class)
    public Response del_news(@ApiParam(value = "资讯主键id") @RequestParam(required = false) int id) {
        newsService.delNews(id);
        return new Response();
    }

    @RequestMapping(value = "/sel_news", method = RequestMethod.GET)
    @ApiOperation(value = "查询资讯信息", notes = "查询资讯信息", response = Response.class)
    public Response sel_news(@ApiParam(value = "资讯主键id") @RequestParam(required = false) int id) {
        Response response = new Response();
        response.setData(newsService.selNews(id));
        return response;
    }

    @RequestMapping(value = "/batch_del_news", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除资讯信息", notes = "批量删除资讯信息", response = Response.class)
    public Response batch_del_news(@ApiParam(value = "要删除的资讯id字符串") @RequestParam(required = false) String ids) {
        newsService.batchDelNews(ids);
        return new Response();
    }

    @RequestMapping(value = "/batch_pub_news", method = RequestMethod.POST)
    @ApiOperation(value = "批量发布资讯信息", notes = "批量发布资讯信息", response = Response.class)
    public Response batch_pub_news(@ApiParam(value = "要发布的资讯id字符串") @RequestParam(required = false) String ids) {
        newsService.batchPubNews(ids);
        return new Response();
    }

    @RequestMapping(value = "/batch_update_rec_place", method = RequestMethod.POST)
    @ApiOperation(value = "批量修改资讯信息推荐位置", notes = "批量修改资讯信息", response = Response.class)
    public Response batch_modifty_rec_place(@ApiParam(value = "要发布的资讯id字符串") @RequestParam(required = false) String ids,
            @ApiParam(value = "要增加(减少)属性字符串") @RequestParam(required = false) String recPlace, @ApiParam(value = "增加(减少)") @RequestParam(required = false) int type) {
        newsService.batchModiftyRecPlace(ids, recPlace, type);
        return new Response();
    }

}
