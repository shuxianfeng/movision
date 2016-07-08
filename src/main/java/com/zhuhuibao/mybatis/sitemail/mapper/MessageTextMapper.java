package com.zhuhuibao.mybatis.sitemail.mapper;

import com.zhuhuibao.mybatis.sitemail.entity.MessageText;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface MessageTextMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MessageText record);

    Long insertSelective(MessageText record);

    MessageText selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MessageText record);

    int updateByPrimaryKey(MessageText record);

    List<Map<String,String>> findAllNewsList(RowBounds rowBounds,Map<String, Object> map);
}