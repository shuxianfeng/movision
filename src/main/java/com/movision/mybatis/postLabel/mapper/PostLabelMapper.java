package com.movision.mybatis.postLabel.mapper;

import com.movision.mybatis.postLabel.entity.PostLabel;

public interface PostLabelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostLabel record);

    int insertSelective(PostLabel record);

    PostLabel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostLabel record);

    int updateByPrimaryKey(PostLabel record);
}