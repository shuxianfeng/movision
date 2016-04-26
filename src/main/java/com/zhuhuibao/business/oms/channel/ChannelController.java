package com.zhuhuibao.business.oms.channel;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.oms.entity.ChannelNews;
import com.zhuhuibao.mybatis.oms.service.ChannelNewsService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/11 0011.
 */
@Controller
public class ChannelController {
    @Autowired
    ChannelNewsService newsService;

    @RequestMapping(value="/rest/oms/addChannelNews", method = RequestMethod.POST)
    public void addChannelNews(HttpServletRequest req, HttpServletResponse response, ChannelNews channelNews) throws JsonGenerationException, JsonMappingException, IOException {
        JsonResult jsonResult = new JsonResult();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(session != null) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            channelNews.setCreateid(new Long(principal.getId()));
            jsonResult = newsService.addChannelNews(channelNews);
        }
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    @RequestMapping(value="/rest/oms/updateChannelNewsByID", method = RequestMethod.POST)
    public void updateChannelNewsByID(HttpServletRequest req, HttpServletResponse response, ChannelNews channelNews) throws JsonGenerationException, JsonMappingException, IOException {
        JsonResult jsonResult = new JsonResult();
        Long createID= ShiroUtil.getCreateID();
        if(createID != null)
        {
            channelNews.setCreateid(createID);
            jsonResult = newsService.updateByPrimaryKeySelective(channelNews);
        }
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 查询栏目信息详情
     * @param req
     * @param response
     * @param id  栏目信息的ID
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value="/rest/oms/queryDetailsByID", method = RequestMethod.GET)
    public void queryDetailsByID(HttpServletRequest req, HttpServletResponse response, Long id) throws JsonGenerationException, JsonMappingException, IOException {
        JsonResult jsonResult = newsService.selectByPrimaryKey(id);
        newsService.updateViews(id);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    @RequestMapping(value="/rest/oms/queryNewsByChannelInfo", method = RequestMethod.GET)
    public void queryNewsByChannelInfo(HttpServletRequest req, HttpServletResponse response, ChannelNews channelNews) throws JsonGenerationException, JsonMappingException, IOException {
        JsonResult jsonResult = new JsonResult();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("channelid", channelNews.getChannelid());
        map.put("sort",channelNews.getSort());
        map.put("status",1);
        if(channelNews.getSort() == 1) {
            map.put("count", 8);
        }else if(channelNews.getSort() == 4)
        {
            map.put("count", 5);
        }
        else if (channelNews.getSort() == 2)
        {
            map.put("count",2);
        }
        else if (channelNews.getSort() == 3)
        {
            map.put("count",3);
        }
        jsonResult = newsService.queryNewsByChannelInfo(map);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 查询所有的人物专访
     * @param req
     * @param response
     * @param channelNews
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value="/rest/oms/queryPersonVisit", method = RequestMethod.GET)
    public void queryPersonVisit(HttpServletRequest req, HttpServletResponse response, ChannelNews channelNews) throws JsonGenerationException, JsonMappingException, IOException {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("channelid", channelNews.getChannelid());
        map.put("sort",channelNews.getSort());
        map.put("status",1);
        JsonResult jsonResult = newsService.queryNewsByChannelInfo(map);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    @RequestMapping(value="/rest/oms/queryViewsByChannel", method = RequestMethod.GET)
    public void queryViewsByChannel(HttpServletRequest req, HttpServletResponse response, ChannelNews channelNews) throws JsonGenerationException, JsonMappingException, IOException {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("channelid", channelNews.getChannelid());
        map.put("sort",channelNews.getSort());
        map.put("status",1);
        map.put("count",5);
        JsonResult jsonResult = newsService.queryViewsByChannel(map);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    /**
     * 查询行业资讯列表
     * @param req
     * @param response
     * @param channelNews
     * @param pageNo
     * @param pageSize
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value="/rest/oms/queryNewsList", method = RequestMethod.GET)
    public void queryNewsList(HttpServletRequest req, HttpServletResponse response, ChannelNews channelNews,String pageNo,String pageSize) throws JsonGenerationException, JsonMappingException, IOException {
        JsonResult jsonResult = new JsonResult();
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<ChannelNews> pager = new Paging<ChannelNews>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("channelid", channelNews.getChannelid());
        map.put("sort",channelNews.getSort());
        map.put("status",1);
        List<ChannelNews> channelList = newsService.findAllNewsList(pager,map);
        pager.result(channelList);
        jsonResult.setData(pager);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }
}
