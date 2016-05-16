package com.zhuhuibao.mybatis.oms.mapper;

import com.zhuhuibao.mybatis.oms.entity.ProjectLinkman;

/**
 *
 */
public interface ProjectLinkmanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProjectLinkman record);

    int insertSelective(ProjectLinkman record);

    ProjectLinkman selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProjectLinkman record);

    int updateByPrimaryKey(ProjectLinkman record);
}