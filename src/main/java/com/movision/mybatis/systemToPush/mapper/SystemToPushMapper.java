package com.movision.mybatis.systemToPush.mapper;

import com.movision.mybatis.systemToPush.entity.SystemToPush;

public interface SystemToPushMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SystemToPush record);

    int insertSelective(SystemToPush record);

    SystemToPush selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SystemToPush record);

    int updateByPrimaryKey(SystemToPush record);
}