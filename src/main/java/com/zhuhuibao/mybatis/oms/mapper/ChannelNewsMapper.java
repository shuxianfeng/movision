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

    List<ChannelNews> queryPersonByChannelInfo(Map<String,Object> channelMap);

    int updateViews(Long id);

    List<ChannelNews> queryViewsByChannel(Map<String,Object> channelMap);
}