package com.zhuhuibao.mybatis.oms.mapper;

import com.zhuhuibao.mybatis.oms.entity.ChannelNews;

import org.apache.ibatis.annotations.Param;
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

    List<Map<String,String>> queryHomepageTechnologyList(Map<String, Object> map);

    List<Map<String,String>> findAllPassNewsByType(RowBounds rowBounds, Map<String, Object> params);

    List<Map<String,String>> selectHotViews(@Param("type") String type, @Param("count") Integer count);

    List<Map<String,String>> selectNewViews(@Param("type") String type, @Param("count") Integer count);

    List<Map<String,String>> selectNewsByType(@Param("type") String type, @Param("sort") String sort, @Param("count") String count);

    ChannelNews selectByID(@Param("id") Long id);

	List<Map> findAllChanNewsList(RowBounds rowBounds,Map<String, Object> map);

	List<Map> queryDetailsById(Long id);
}