package com.movision.mybatis.accusation.mapper;

import com.movision.mybatis.accusation.entity.Accusation;

import java.util.List;
import java.util.Map;

public interface AccusationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Accusation record);

    int insertSelective(Accusation record);

    Accusation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Accusation record);

    int updateByPrimaryKey(Accusation record);

    int insertAccusation(Accusation acc);

    List<Accusation> queryAccusationByUserSum(Map map);
}