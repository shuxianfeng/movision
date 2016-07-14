package com.zhuhuibao.mybatis.common.mapper;

import com.zhuhuibao.mybatis.common.entity.SysResearch;

public interface SysResearchMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysResearch record);

    int insertSelective(SysResearch record);

    SysResearch selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysResearch record);

    int updateByPrimaryKey(SysResearch record);
}