package com.movision.mybatis.postProcessRecord.mapper;

import com.movision.mybatis.postProcessRecord.entity.PostProcessRecord;

public interface PostProcessRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostProcessRecord record);

    int insertSelective(PostProcessRecord record);

    PostProcessRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostProcessRecord record);

    int updateByPrimaryKey(PostProcessRecord record);

    PostProcessRecord queryPostByIsessenceOrIshot(Integer postid);

    void updateProcessRecord(PostProcessRecord postProcessRecord);

    void insertProcessRecord(PostProcessRecord postProcessRecord);
}