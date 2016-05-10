package com.zhuhuibao.mybatis.oms.service;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.MsgCodeConstant;
import com.zhuhuibao.mybatis.oms.entity.ChannelNews;
import com.zhuhuibao.mybatis.oms.mapper.ChannelNewsMapper;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
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

    /**
     * 增加频道信息
     * @param news
     * @return
     */
    public JsonResult addChannelNews(ChannelNews news)
    {
        JsonResult jsonResult = new JsonResult();
        try
        {
            int result = channel.insertSelective(news);
        }
        catch(Exception e)
        {
            log.error("add channel news error!",e);
        }
        return jsonResult;
    }

    /**
     * 更新频道信息
     * @param channelNews  频道信息
     * @return
     */
    public JsonResult updateByPrimaryKeySelective(ChannelNews channelNews)
    {
        JsonResult jsonResult = new JsonResult();
        try
        {
            channel.updateByPrimaryKeySelective(channelNews);
        }
        catch(Exception e)
        {
            log.error("add channel news error!",e);
            jsonResult.setCode(MsgCodeConstant.response_status_400);
            jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
            jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
        }
        return jsonResult;
    }
    /**
     * 更新点击率
     * @param id  频道ID
     * @return
     */
    public JsonResult updateViews(Long id)
    {
        JsonResult jsonResult = new JsonResult();
        try
        {
            channel.updateViews(id);
        }
        catch(Exception e)
        {
            log.error("add channel news error!",e);
            jsonResult.setCode(MsgCodeConstant.response_status_400);
            jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
            jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
        }
        return jsonResult;
    }

    /**
     * 查询频道信息
     * @param id  频道信息
     * @return
     */
    public JsonResult selectByPrimaryKey(Long id)
    {
        JsonResult jsonResult = new JsonResult();
        try
        {
            ChannelNews news =  channel.selectByPrimaryKey(id);
            jsonResult.setData(news);
        }
        catch(Exception e)
        {
            log.error("select by primary key error!",e);
            jsonResult.setCode(MsgCodeConstant.response_status_400);
            jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
            jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
        }
        return jsonResult;
    }

    /**
     * 查询资讯信息根据频道ID
     * @param channelMap  频道资讯页条件
     * @return JsonResult
     */
    public JsonResult queryNewsByChannelInfo(Map<String,Object> channelMap )
    {
        JsonResult jsonResult = new JsonResult();
        try
        {
            List<ChannelNews> newsList =  channel.queryNewsByChannelInfo(channelMap);
            jsonResult.setData(newsList);
        }
        catch(Exception e)
        {
            log.error("select by primary key error!",e);
            jsonResult.setCode(MsgCodeConstant.response_status_400);
            jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
            jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
        }
        return jsonResult;
    }

    public List<ChannelNews> findAllNewsByChannelInfo(Paging<ChannelNews> pager,Map<String,Object> channelMap)
    {
          return channel.findAllNewsByChannelInfo(pager.getRowBounds(),channelMap);
    }

    /**
     * 查询主频道某个栏目点击率排行
     * @param channelMap  频道资讯页条件
     * @return JsonResult
     */
    public JsonResult queryViewsByChannel(Map<String,Object> channelMap )
    {
        JsonResult jsonResult = new JsonResult();
        try
        {
            List<ChannelNews> newsList =  channel.queryViewsByChannel(channelMap);
            jsonResult.setData(newsList);
        }
        catch(Exception e)
        {
            log.error("select by primary key error!",e);
            jsonResult.setCode(MsgCodeConstant.response_status_400);
            jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
            jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
        }
        return jsonResult;
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

}
