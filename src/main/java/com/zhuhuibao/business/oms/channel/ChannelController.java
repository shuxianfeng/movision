package com.zhuhuibao.business.oms.channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.oms.entity.ChannelNews;
import com.zhuhuibao.mybatis.oms.service.ChannelNewsService;
import com.zhuhuibao.shiro.realm.OMSRealm;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

/**
 * Created by Administrator on 2016/4/11 0011.
 */
@RestController
public class ChannelController {
    private static final Logger log = LoggerFactory.getLogger(ChannelController.class);

    @Autowired
    ChannelNewsService newsService;

    @RequestMapping(value = "/rest/oms/addChannelNews", method = RequestMethod.POST)
    public Response addChannelNews(@ModelAttribute ChannelNews channelNews) throws IOException {
        Response response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (session != null) {
            OMSRealm.ShiroOmsUser principal = (OMSRealm.ShiroOmsUser) session.getAttribute("oms");
            channelNews.setCreateid(new Long(principal.getId()));
            response = newsService.addChannelNews(channelNews);
        }

        return response;
    }

    @RequestMapping(value = "/rest/oms/updateChannelNewsByID", method = RequestMethod.POST)
    public Response updateChannelNewsByID(ChannelNews channelNews) throws IOException {
        Response response = new Response();
        Long createID = ShiroUtil.getOmsCreateID();

        if (createID != null) {
            channelNews.setCreateid(createID);
            response = newsService.updateByPrimaryKeySelective(channelNews);
        } else {

            response.setCode(400);
            response.setMessage("更新失败");

        }

        return response;
    }

    /**
     * 查询栏目信息详情
     *
     * @param id       栏目信息的ID
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/rest/oms/queryDetailsByID", method = RequestMethod.GET)
    public Response queryDetailsByID(Long id) throws IOException {
        Response response = newsService.selectByPrimaryKey(id);
        newsService.updateViews(id);
        return response;
    }

    @RequestMapping(value = "/rest/oms/queryNewsByChannelInfo", method = RequestMethod.GET)
    public Response queryNewsByChannelInfo(ChannelNews channelNews) throws IOException {
        Response response = new Response();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("channelid", channelNews.getChannelid());
        map.put("sort", channelNews.getSort());
        map.put("status", 1);
        if (channelNews.getSort() == 1) {
            map.put("count", 8);
        } else if (channelNews.getSort() == 4) {
            map.put("count", 5);
        } else if (channelNews.getSort() == 2) {
            map.put("count", 2);
        } else if (channelNews.getSort() == 3) {
            map.put("count", 3);
        }else if (channelNews.getChannelid() == 11) {//技术频道新技术播报
            map.put("count", 5);
        }
        List<ChannelNews> newsList = newsService.queryNewsByChannelInfo(map);
        response.setData(newsList);
        return response;
    }

    /**
     * 查询所有的人物专访
     *
     * @param channelNews
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/rest/oms/queryPersonVisit", method = RequestMethod.GET)
    public Response queryPersonVisit(ChannelNews channelNews) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("channelid", channelNews.getChannelid());
        map.put("sort", channelNews.getSort());
        map.put("status", 1);
        List<ChannelNews> newsList = newsService.queryNewsByChannelInfo(map);
        Response response = new Response();
        response.setData(newsList);
        return response;
    }

    @RequestMapping(value = "/rest/oms/queryViewsByChannel", method = RequestMethod.GET)
    @ApiOperation(value = "查询资讯点击率排行",notes = "查询资讯点击率排行",response = Response.class)
    public Response queryViewsByChannel(ChannelNews channelNews) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("channelid", channelNews.getChannelid());
        map.put("sort", channelNews.getSort());
        map.put("status", 1);
        map.put("count", 5);
        Response response = newsService.queryViewsByChannel(map);
        return response;
    }

    /**
     * 查询行业资讯列表
     *
     * @param channelNews
     * @param pageNo
     * @param pageSize
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/rest/oms/queryNewsList", method = RequestMethod.GET)
    public Response queryNewsList(ChannelNews channelNews, String pageNo, String pageSize) throws IOException {
        Response response = new Response();
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<ChannelNews> pager = new Paging<ChannelNews>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("channelid", channelNews.getChannelid());
        map.put("sort", channelNews.getSort());
        map.put("status", 1);
        List<ChannelNews> channelList = newsService.findAllNewsList(pager, map);
        pager.result(channelList);
        response.setData(pager);

        return response;
    }

    /**
     * 查询资讯内容列表
     *
     * @param channelNews
     * @param pageNo
     * @param pageSize
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/rest/oms/queryContentList", method = RequestMethod.GET)
    public Response queryContentList(ChannelNews channelNews, String pageNo, String pageSize) throws IOException {
        Response response = new Response();
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<ChannelNews> pager = new Paging<ChannelNews>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", channelNews.getTitle());
        map.put("channelid", channelNews.getChannelid());
        map.put("sort", channelNews.getSort());
        map.put("status", channelNews.getStatus());
        List<ChannelNews> channelList = newsService.findAllContentList(pager, map);
        pager.result(channelList);
        response.setData(pager);

        return response;
    }

    /**
     * 查询资讯内容列表
     *
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = "/rest/oms/queryChannelList", method = RequestMethod.GET)
    public Response queryChannelList() throws IOException {
        Response response = new Response();
        response = newsService.queryChannelList();
        return response;
    }

    /**
     * 批量删除咨询信息
     *
     * @param req
     * @return
     * @throws IOException
     */

    @RequestMapping(value = "/rest/oms/batchDelNews", method = RequestMethod.POST)
    public Response batchDelNews(HttpServletRequest req) throws IOException {
        String ids[] = req.getParameterValues("ids");
        Response result = new Response();
        for (String id : ids) {
            int isdelete = newsService.batchDelNews(id);
            if (isdelete == 0) {
                result.setCode(400);
                result.setMessage("删除失败");
            } else {
                result.setCode(200);
            }
        }

        return result;
    }
}
