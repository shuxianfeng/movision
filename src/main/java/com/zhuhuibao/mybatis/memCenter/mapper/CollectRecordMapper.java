package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.CollectRecord;

public interface CollectRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CollectRecord record);

    int insertSelective(CollectRecord record);

    CollectRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CollectRecord record);

    int updateByPrimaryKey(CollectRecord record);
}