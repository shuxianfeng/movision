package com.movision.mybatis.period.mapper;

import com.movision.mybatis.period.entity.Period;

import java.util.List;
import java.util.Map;

public interface PeriodMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Period record);

    int insertSelective(Period record);

    Period selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Period record);

    int updateByPrimaryKey(Period record);

    int insertSelective(Map map);

    int insertSelectiveTwo(Period record);

    Period findAllPeriod(Integer postid);

}