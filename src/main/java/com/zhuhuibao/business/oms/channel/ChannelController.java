package com.zhuhuibao.business.oms.channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.oms.entity.ChannelNews;
import com.zhuhuibao.mybatis.oms.service.ChannelNewsService;
import com.zhuhuibao.shiro.realm.OMSRealm;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

/**
 * Created by Administrator on 2016/4/11 0011.
 */
@Controller
public class ChannelController {
    @Autowired
    ChannelNewsService newsService;
    private static final Logger log = LoggerFactory.getLogger(ChannelController.class);
    @RequestMapping(value="/rest/oms/addChannelNews", method = RequestMethod.POST)
    public void addChannelNews(HttpServletRequest req, HttpServletResponse response, ChannelNews channelNews) throws JsonGenerationException, JsonMappingException, IOException {
        JsonResult jsonResult = new JsonResult();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(session != null) {
            OMSRealm.ShiroOmsUser principal = (OMSRealm.ShiroOmsUser) session.getAttribute("oms");
            channelNews.setCreateid(new Long(principal.getId()));
            jsonResult = newsService.addChannelNews(channelNews);
        }
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }

    @RequestMapping(value="/rest/oms/updateChannelNewsByID", method = RequestMethod.POST)
    public void updateChannelNewsByID(HttpServletRequest req, HttpServletResponse response, ChannelNews channelNews) throws JsonGenerationException, JsonMappingException, IOException {
        JsonResult jsonResult = new JsonResult();
        Long createID= ShiroUtil.getOmsCreateID();
       
        if(createID != null)
        {
            channelNews.setCreateid(createID);
            jsonResult = newsService.updateByPrimaryKeySelective(channelNews);
        }else{
        	 
        		jsonResult.setCode(400);
				jsonResult.setMessage("更新失败");
		 
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
    
    /**
     * 查询资讯内容列表
     * @param req
     * @param response
     * @param channelNews
     * @param pageNo
     * @param pageSize
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value="/rest/oms/queryContentList", method = RequestMethod.GET)
    public void queryContentList(HttpServletRequest req, HttpServletResponse response, ChannelNews channelNews,String pageNo,String pageSize) throws JsonGenerationException, JsonMappingException, IOException {
        JsonResult jsonResult = new JsonResult();
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<ChannelNews> pager = new Paging<ChannelNews>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("title", channelNews.getTitle());
        map.put("channelid",channelNews.getChannelid());
        map.put("status",channelNews.getStatus());
        List<ChannelNews> channelList = newsService.findAllContentList(pager,map);
        pager.result(channelList);
        jsonResult.setData(pager);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }
    
    /**
     * 查询资讯内容列表
     * @param req
     * @param response
     * @param channelNews
     * @param pageNo
     * @param pageSize
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value="/rest/oms/queryChannelList", method = RequestMethod.GET)
    public void queryChannelList(HttpServletRequest req, HttpServletResponse response, ChannelNews channelNews,String pageNo,String pageSize) throws JsonGenerationException, JsonMappingException, IOException {
        JsonResult jsonResult = new JsonResult();
        jsonResult = newsService.queryChannelList(); 
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
         
    }
    
    /**
	 * 批量删除咨询信息
	 * @param req
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/oms/batchDelNews", method = RequestMethod.POST)
	public void batchDelNews(HttpServletRequest req, HttpServletResponse response) throws IOException {
		String ids[] = req.getParameterValues("ids");
		JsonResult result = new JsonResult();
		for(int i=0;i<ids.length;i++){
			String id = ids[i];
			int isdelete = newsService.batchDelNews(id);
			if(isdelete==0){
				result.setCode(400);
				result.setMessage("删除失败");
			}else{
				result.setCode(200);
			}
		}
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
	}
}
