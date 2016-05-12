package com.zhuhuibao.mybatis.oms.mapper;

import com.zhuhuibao.mybatis.oms.entity.ProjectPartyA;

public interface ProjectPartyAMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProjectPartyA record);

    int insertSelective(ProjectPartyA record);

    ProjectPartyA selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProjectPartyA record);

    int updateByPrimaryKey(ProjectPartyA record);
}