package com.movision.mybatis.postCommentZanRecord.mapper;

import com.movision.mybatis.postCommentZanRecord.entity.PostCommentZanRecord;

public interface PostCommentZanRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostCommentZanRecord record);

    int insertSelective(PostCommentZanRecord record);

    PostCommentZanRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostCommentZanRecord record);

    int updateByPrimaryKey(PostCommentZanRecord record);
}