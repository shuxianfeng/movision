package com.movision.mybatis.imSystemInformRead.mapper;

import com.movision.mybatis.imSystemInformRead.entity.ImSystemInformRead;

import java.util.Map;

public interface ImSystemInformReadMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ImSystemInformRead record);

    int insertSelective(ImSystemInformRead record);

    ImSystemInformRead selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImSystemInformRead record);

    int updateByPrimaryKey(ImSystemInformRead record);

    Integer insertSystemRead(Map map);

    ImSystemInformRead queryInfromRead(Map map);

    Integer updateSystemRead(Map map);

    Integer queryUserCheckPush(Map map);

    Integer wholeSignRead(Map map);
}