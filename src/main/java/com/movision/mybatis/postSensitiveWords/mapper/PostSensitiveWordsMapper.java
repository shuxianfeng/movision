package com.movision.mybatis.postSensitiveWords.mapper;

import com.movision.mybatis.postSensitiveWords.entity.PostSensitiveWords;

public interface PostSensitiveWordsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostSensitiveWords record);

    int insertSelective(PostSensitiveWords record);

    PostSensitiveWords selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostSensitiveWords record);

    int updateByPrimaryKey(PostSensitiveWords record);
}