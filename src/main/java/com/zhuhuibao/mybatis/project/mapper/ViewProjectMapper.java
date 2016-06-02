package com.zhuhuibao.mybatis.project.mapper;

import com.zhuhuibao.mybatis.project.entity.ViewProject;

import java.util.Map;

/**
 * 我查看过的项目信息DAO层
 * @author  penglong
 * @create 2016-05-27
 */
public interface ViewProjectMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ViewProject record);

    int insertSelective(ViewProject record);

    ViewProject selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ViewProject record);

    int updateByPrimaryKey(ViewProject record);

    int checkIsViewProject(Map<String,Object> map);
}