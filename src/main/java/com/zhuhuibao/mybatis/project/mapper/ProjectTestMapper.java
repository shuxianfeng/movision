package com.zhuhuibao.mybatis.project.mapper;

import com.zhuhuibao.mybatis.project.entity.ProjectTest;

import java.util.List;
import java.util.Map;

public interface ProjectTestMapper {
    int insertSelective(ProjectTest record);

    ProjectTest selectByPrimaryKey(Integer id);

    List<Map<String,String>> queryAll(Map<String, Object> map);
}