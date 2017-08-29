package com.movision.mybatis.complainsrecords.mapper;

import com.movision.mybatis.complainsrecords.entity.Complainsrecords;

public interface ComplainsrecordsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Complainsrecords record);

    int insertSelective(Complainsrecords record);

    Complainsrecords selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Complainsrecords record);

    int updateByPrimaryKey(Complainsrecords record);
}