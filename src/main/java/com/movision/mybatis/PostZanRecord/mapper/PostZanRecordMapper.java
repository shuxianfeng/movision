package com.movision.mybatis.PostZanRecord.mapper;

import com.movision.mybatis.PostZanRecord.entity.PostZanRecord;

public interface PostZanRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostZanRecord record);

    int insertSelective(PostZanRecord record);

    PostZanRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostZanRecord record);

    int updateByPrimaryKey(PostZanRecord record);
}