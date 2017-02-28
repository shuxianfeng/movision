package com.movision.mybatis.pointRecord.mapper;

import com.movision.mybatis.pointRecord.entity.PointRecord;

public interface PointRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PointRecord record);

    int insertSelective(PointRecord record);

    PointRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PointRecord record);

    int updateByPrimaryKey(PointRecord record);
}