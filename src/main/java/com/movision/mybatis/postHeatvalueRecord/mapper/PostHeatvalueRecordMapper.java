package com.movision.mybatis.postHeatvalueRecord.mapper;

import com.movision.mybatis.postHeatvalueRecord.entity.PostHeatvalueRecord;

public interface PostHeatvalueRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostHeatvalueRecord record);

    int insertSelective(PostHeatvalueRecord record);

    PostHeatvalueRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostHeatvalueRecord record);

    int updateByPrimaryKey(PostHeatvalueRecord record);
}