package com.zhuhuibao.mobile.web;

import java.util.List;
import java.util.Map;

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
import com.zhuhuibao.service.NewsService;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * 资讯业务控制层
 *
 * @author liyang
 * @date 2016年10月20日
 */
@RestController
@RequestMapping("/rest/m/news/site/")
@Api(value = "mobileNews", description = "资讯")
public class MobileNewsController {

    private static final Logger log = LoggerFactory.getLogger(MobileNewsController.class);

    @Autowired
    private NewsService newsService;

    /**
     * 触屏端资讯-列表页面
     * 
     * @param queryType
     *            列表类型:1 全部，2 热点，3 分类
     * @param type
     *            一级分类:1 行业资讯,2 专题,3 筑慧访谈,4 曝光台,5 工程商新闻,6 深度观察,7 活动
     * @param subtype
     *            行业资讯下面的二级分类:1 网络及硬件,2 安全防范,3 楼宇自动化,4 数据中心,5 智能家居,6 影音视频,7
     *            应用系统,8 智能照明,9 行业软件
     * @param pageNo
     *            当前页码
     * @param pageSize
     *            每页多少数据
     * @return
     */
    @ApiOperation(value = "触屏端资讯-列表页面", notes = "触屏端资讯-列表页面")
    @RequestMapping(value = "sel_news_list", method = RequestMethod.GET)
    public Response sel_news_list(@ApiParam(value = "列表类型:1 全部，2 热点，3 分类") @RequestParam(required = true) String queryType,
            @ApiParam(value = "一级分类:1 行业资讯,2 专题,3 筑慧访谈,4 曝光台,5 工程商新闻,6 深度观察,7 活动") @RequestParam(required = true) String type,
            @ApiParam(value = "二级分类:1 网络及硬件,2 安全防范,3 楼宇自动化,4 数据中心,5 智能家居,6 影音视频,7 应用系统,8 智能照明,9 行业软件") @RequestParam(required = true) String subtype,
            @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") int pageNo, @ApiParam(value = "每页显示的数目") @RequestParam(required = false, defaultValue = "10") int pageSize) {
        Response response = new Response();
        Paging<NewsForm> pager = new Paging<>(pageNo, pageSize);
        List<NewsForm> forms = newsService.mobile_sel_news_list(queryType, type, subtype, pager);
        response.setData(forms);
        return response;
    }


    /**
     * 触屏端资讯-详情页面
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/sel_news_info", method = RequestMethod.GET)
    @ApiOperation(value = "触屏端资讯-详情页面", notes = "触屏端资讯-详情页面", response = Response.class)
    public Response sel_news(@ApiParam(value = "资讯主键id") @RequestParam(required = false) int id) {
        Response response = new Response();
        response.setData(newsService.sel_news(id));
        return response;
    }
}
