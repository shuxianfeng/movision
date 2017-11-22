package com.movision.mybatis.imUserAccusation.mapper;

import com.movision.mybatis.imUserAccusation.entity.ImUserAccusation;

import java.util.List;

public interface ImUserAccusationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ImUserAccusation record);

    int insertSelective(ImUserAccusation record);

    ImUserAccusation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImUserAccusation record);

    int updateByPrimaryKey(ImUserAccusation record);

    List<ImUserAccusation> queryNotHandleSelectiveRecord(ImUserAccusation imUserAccusation);
}