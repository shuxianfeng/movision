package com.zhuhuibao.mybatis.oms.mapper;

import com.zhuhuibao.mybatis.oms.entity.ChannelNews;

import org.apache.ibatis.session.RowBounds;

import java.util.*;

public interface ChannelNewsMapper {

    int insertSelective(ChannelNews record);

    ChannelNews selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ChannelNews record);

    List<ChannelNews> queryNewsByChannelInfo(Map<String,Object> channelMap);

    List<ChannelNews> findAllNewsList(RowBounds rowBounds, Map<String,Object> channelMap);

    int updateViews(Long id);

    List<ChannelNews> queryViewsByChannel(Map<String,Object> channelMap);
    //查询咨询列表
    List<ChannelNews> findAllContentList(RowBounds rowBounds, Map<String,Object> channelMap);
    //查询所属项目
    List<ChannelNews> queryChannelList();

	int batchDelNews(String id);

    List<Map<String,Object>> findAllTechNewsList(RowBounds rowBounds, Map<String,Object> channelMap);

    Map<String,Object> previewNewsInfo(Map<String,Object> channelMap);
}