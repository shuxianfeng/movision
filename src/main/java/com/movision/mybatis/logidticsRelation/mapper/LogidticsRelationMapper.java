package com.movision.mybatis.logidticsRelation.mapper;

import com.movision.mybatis.logidticsRelation.entity.LogidticsRelation;

public interface LogidticsRelationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LogidticsRelation record);

    int insertSelective(LogidticsRelation record);

    LogidticsRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LogidticsRelation record);

    int updateByPrimaryKey(LogidticsRelation record);
}