package com.movision.mybatis.imLoginRecord.mapper;

import com.movision.mybatis.imLoginRecord.entity.ImLoginRecord;

public interface ImLoginRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ImLoginRecord record);

    int insertSelective(ImLoginRecord record);

    ImLoginRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImLoginRecord record);

    int updateByPrimaryKey(ImLoginRecord record);

    ImLoginRecord queryRecordByaccidAndTimestamp(ImLoginRecord record);
}