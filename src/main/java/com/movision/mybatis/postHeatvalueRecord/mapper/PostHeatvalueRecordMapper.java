package com.movision.mybatis.postHeatvalueRecord.mapper;

import com.movision.mybatis.postHeatvalueRecord.entity.PostHeatvalueRecord;

import java.util.List;
import java.util.Map;

public interface PostHeatvalueRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostHeatvalueRecord record);

    int insertSelective(PostHeatvalueRecord record);

    PostHeatvalueRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostHeatvalueRecord record);

    int updateByPrimaryKey(PostHeatvalueRecord record);

    List<PostHeatvalueRecord> querySpecifyDatePostHeatvalueRecord(Map map);
}