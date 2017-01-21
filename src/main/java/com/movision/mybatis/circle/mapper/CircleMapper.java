package com.movision.mybatis.circle.mapper;

import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.circle.entity.CircleVo;

import java.util.List;

public interface CircleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Circle record);

    int insertSelective(Circle record);

    Circle selectByPrimaryKey(Integer id);

    List<CircleVo> queryHotCircleList();

    CircleVo queryCircleIndex1(int circleid);

    int updateByPrimaryKeySelective(Circle record);

    int updateByPrimaryKey(Circle record);
}