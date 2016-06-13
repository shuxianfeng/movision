package com.zhuhuibao.mybatis.sitemail.mapper;

import com.zhuhuibao.mybatis.sitemail.entity.MessageLog;

import java.util.Map;

public interface MessageLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MessageLog record);

    int insertSelective(MessageLog record);

    MessageLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MessageLog record);

    int updateByPrimaryKey(MessageLog record);

    Integer queryUnreadMsgCount(Map<String,Object> map);

    int addNewsToLog(Map<String,Object> map);

    Integer selUnreadNewsCount(Map<String,Object> map);
}