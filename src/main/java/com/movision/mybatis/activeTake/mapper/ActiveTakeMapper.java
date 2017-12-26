package com.movision.mybatis.activeTake.mapper;

import com.movision.mybatis.activePart.entity.ActivePart;
import com.movision.mybatis.activeTake.entity.ActiveTake;
import com.movision.mybatis.period.entity.Period;

import java.util.List;
import java.util.Map;

public interface ActiveTakeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ActiveTake record);

    int insertSelective(ActiveTake record);

    ActiveTake selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ActiveTake record);

    int updateByPrimaryKey(ActiveTake record);


    int takeActive(ActiveTake activeTake);

    int deviceCount(Map map);

    int postidCount(Map map);

    int takeCount(Map map);


    int activeid(Integer postid);

    Period queryActiveTime(Integer postid);

 }