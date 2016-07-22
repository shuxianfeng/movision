package com.zhuhuibao.mybatis.project.mapper;

import com.zhuhuibao.mybatis.project.entity.ProjectTest;

public interface ProjectTestMapper {
    int insertSelective(ProjectTest record);

    ProjectTest selectByPrimaryKey(Integer id);

}