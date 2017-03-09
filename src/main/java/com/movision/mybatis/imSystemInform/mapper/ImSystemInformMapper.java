package com.movision.mybatis.imSystemInform.mapper;

import com.movision.mybatis.imSystemInform.entity.ImSystemInform;

public interface ImSystemInformMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ImSystemInform record);

    int insertSelective(ImSystemInform record);

    ImSystemInform selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImSystemInform record);

    int updateByPrimaryKey(ImSystemInform record);
}