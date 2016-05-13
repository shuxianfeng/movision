package com.zhuhuibao.mybatis.oms.mapper;

import com.zhuhuibao.mybatis.oms.entity.ProjectPartyB;

public interface ProjectPartyBMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProjectPartyB record);

    int insertSelective(ProjectPartyB record);

    ProjectPartyB selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProjectPartyB record);

    int updateByPrimaryKey(ProjectPartyB record);
}