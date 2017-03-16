package com.movision.mybatis.systemPush.mapper;

import com.movision.mybatis.systemPush.entity.SystemPush;

public interface SystemPushMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SystemPush record);

    int insertSelective(SystemPush record);

    SystemPush selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SystemPush record);

    int updateByPrimaryKey(SystemPush record);
}