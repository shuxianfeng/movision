package com.movision.mybatis.systemLayout.mapper;

import com.movision.mybatis.systemLayout.entity.SystemLayout;

public interface SystemLayoutMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SystemLayout record);

    int insertSelective(SystemLayout record);

    SystemLayout selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SystemLayout record);

    int updateByPrimaryKey(SystemLayout record);

    Integer queryRobotSeparate(String separate);

    Double queryRobotpercentage(String percentage);

    String queryServiceUrl(String fileurl);

    Integer queryFileRatio(String ratio);
}