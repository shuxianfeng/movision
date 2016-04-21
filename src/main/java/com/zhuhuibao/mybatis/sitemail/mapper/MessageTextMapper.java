package com.zhuhuibao.mybatis.sitemail.mapper;

import com.zhuhuibao.mybatis.sitemail.entity.MessageText;

public interface MessageTextMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MessageText record);

    int insertSelective(MessageText record);

    MessageText selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MessageText record);

    int updateByPrimaryKey(MessageText record);
}