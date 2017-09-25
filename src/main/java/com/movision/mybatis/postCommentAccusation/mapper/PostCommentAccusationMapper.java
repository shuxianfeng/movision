package com.movision.mybatis.postCommentAccusation.mapper;

import com.movision.mybatis.postCommentAccusation.entity.PostCommentAccusation;

public interface PostCommentAccusationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostCommentAccusation record);

    int insertSelective(PostCommentAccusation record);

    PostCommentAccusation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostCommentAccusation record);

    int updateByPrimaryKey(PostCommentAccusation record);
}