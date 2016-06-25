package com.zhuhuibao.mybatis.oms.service;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.oms.entity.ChannelNews;
import com.zhuhuibao.mybatis.oms.mapper.ChannelNewsMapper;
import com.zhuhuibao.mybatis.sitemail.service.SiteMailService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;

import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/12 0012.
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
@Transactional
public class ChannelNewsService {
    private static final Logger log = LoggerFactory.getLogger(ChannelNewsService.class);

    @Autowired
    ChannelNewsMapper channel;

    @Autowired
    SiteMailService siteMailService;

    /**
     * 增加频道信息
     * @param news
     * @return
     */
    public Response addChannelNews(ChannelNews news)
    {
        Response response = new Response();
        try
        {
            int result = channel.insertSelective(news);
        }
        catch(Exception e)
        {
            log.error("add channel news error!",e);
        }
        return response;
    }

    /**
     * 更新频道信息
     * @param channelNews  频道信息
     * @return
     */
    public Response updateByPrimaryKeySelective(ChannelNews channelNews)
    {
        Response response = new Response();
        try
        {
            channel.updateByPrimaryKeySelective(channelNews);
            if("2".equals(String.valueOf(channelNews.getStatus())))
            {
                siteMailService.addRefuseReasonMail(ShiroUtil.getOmsCreateID(),channelNews.getCreateid(),channelNews.getReason());
            }
        }
        catch(Exception e)
        {
            log.error("add channel news error!",e);
            response.setCode(MsgCodeConstant.response_status_400);
            response.setMsgCode(MsgCodeConstant.mcode_common_failure);
            response.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
        }
        return response;
    }
    /**
     * 更新点击率
     * @param id  频道ID
     * @return
     */
    public Response updateViews(Long id)
    {
        Response response = new Response();
        try
        {
            channel.updateViews(id);
        }
        catch(Exception e)
        {
            log.error("add channel news error!",e);
            response.setCode(MsgCodeConstant.response_status_400);
            response.setMsgCode(MsgCodeConstant.mcode_common_failure);
            response.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
        }
        return response;
    }

    /**
     * 查询频道信息
     * @param id  频道信息
     * @return
     */
    public Response selectByPrimaryKey(Long id)
    {
        Response response = new Response();
        try
        {
            ChannelNews news =  channel.selectByPrimaryKey(id);
            response.setData(news);
        }
        catch(Exception e)
        {
            log.error("select by primary key error!",e);
            response.setCode(MsgCodeConstant.response_status_400);
            response.setMsgCode(MsgCodeConstant.mcode_common_failure);
            response.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
        }
        return response;
    }

    /**
     * 查询资讯信息根据频道ID
     * @param channelMap  频道资讯页条件
     * @return Response
     */
    public List<ChannelNews> queryNewsByChannelInfo(Map<String,Object> channelMap )
    {
        List<ChannelNews> newsList;
        try
        {
            newsList =  channel.queryNewsByChannelInfo(channelMap);
        }
        catch(Exception e)
        {
            log.error("select by primary key error!",e);
            throw e;
        }
        return newsList;
    }

    /**
     * 查询主频道某个栏目点击率排行
     * @param channelMap  频道资讯页条件
     * @return Response
     */
    public Response queryViewsByChannel(Map<String,Object> channelMap )
    {
        Response response = new Response();
        try
        {
            List<ChannelNews> newsList =  channel.queryViewsByChannel(channelMap);
            response.setData(newsList);
        }
        catch(Exception e)
        {
            log.error("select by primary key error!",e);
            response.setCode(MsgCodeConstant.response_status_400);
            response.setMsgCode(MsgCodeConstant.mcode_common_failure);
            response.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
        }
        return response;
    }

    /**
     * 查询资讯信息根据频道ID
     * @param channelMap  频道资讯页条件
     * @return List<ChannelNews>
     */
    public List<ChannelNews> findAllNewsList(Paging<ChannelNews> pager,Map<String, Object> channelMap )
    {
        List<ChannelNews> newsList = null;
        try
        {
            newsList =  channel.findAllNewsList(pager.getRowBounds(),channelMap);
        }
        catch(Exception e)
        {
            log.error("find all news List pager error!",e);
        }
        return newsList;
    }

    /**
     * 查询技术频道的新技术播报
     * @param channelMap  频道资讯页条件
     * @return List<ChannelNews>
     */
    public List<Map<String,Object>> findAllTechNewsList(Paging<Map<String,Object>> pager,Map<String, Object> channelMap )
    {
        List<Map<String,Object>> newsList = null;
        try
        {
            newsList =  channel.findAllTechNewsList(pager.getRowBounds(),channelMap);
        }
        catch(Exception e)
        {
            log.error("find all news List pager error!",e);
        }
        return newsList;
    }

    /**
     * 预览技术频道的新技术播报
     * @param channelMap  频道资讯页条件
     * @return List<ChannelNews>
     */
    public Map<String,Object> previewNewsInfo(Map<String, Object> channelMap)
    {
        log.info("preview tech news info "+ StringUtils.mapToString(channelMap));
        Map<String,Object> newsList = null;
        try
        {
            newsList =  channel.previewNewsInfo(channelMap);
        }
        catch(Exception e)
        {
            log.error("preview tech news info error!",e);
        }
        return newsList;
    }
    
    /**
     * 查询资讯内容管理
     * @param channelMap  频道资讯页条件
     * @return List<ChannelNews>
     */
    public List<ChannelNews> findAllContentList(Paging<ChannelNews> pager,Map<String, Object> channelMap )
    {
        List<ChannelNews> newsList = null;
        try
        {
            newsList =  channel.findAllContentList(pager.getRowBounds(),channelMap);
        }
        catch(Exception e)
        {
            log.error("find all news List pager error!",e);
        }
        return newsList;
    }
    
    /**
     * 查询所说项目
     * @return Response
     */
    public Response queryChannelList()
    {
        Response response = new Response();
        try
        {
            List<ChannelNews> newsList =  channel.queryChannelList();
            response.setData(newsList);
        }
        catch(Exception e)
        {
            log.error("select by primary key error!",e);
            response.setCode(MsgCodeConstant.response_status_400);
            response.setMsgCode(MsgCodeConstant.mcode_common_failure);
            response.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
        }
        return response;
    }
    /**
     * 删除咨询信息
     * @param id  频道资讯页条件
     * @return int
     */
	public int batchDelNews(String id) {
		log.debug("删除咨询信息");
		int result = 0;
		result = channel.batchDelNews(id);
		return result;
	}

    public List<Map<String,String>> queryHomepageTechnologyList(Map<String, Object> map){
        try{
            return channel.queryHomepageTechnologyList(map);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
