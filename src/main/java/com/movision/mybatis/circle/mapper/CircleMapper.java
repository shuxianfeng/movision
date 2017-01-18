package com.movision.mybatis.circle.mapper;

import com.movision.mybatis.circle.entity.Circle;

public interface CircleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Circle record);

    int insertSelective(Circle record);

    Circle selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Circle record);

    int updateByPrimaryKey(Circle record);
}