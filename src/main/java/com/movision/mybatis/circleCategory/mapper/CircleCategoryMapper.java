package com.movision.mybatis.circleCategory.mapper;

import com.movision.mybatis.circleCategory.entity.CircleCategory;

import java.util.List;

public interface CircleCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CircleCategory record);

    int insertSelective(CircleCategory record);

    CircleCategory selectByPrimaryKey(Integer id);

    List<CircleCategory> queryCircleCategoryList();

    int updateByPrimaryKeySelective(CircleCategory record);

    int updateByPrimaryKey(CircleCategory record);
}