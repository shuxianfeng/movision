package com.movision.mybatis.postLabelRelation.mapper;

import com.movision.mybatis.postLabelRelation.entity.PostLabelRelation;

public interface PostLabelRelationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostLabelRelation record);

    int insertSelective(PostLabelRelation record);

    PostLabelRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostLabelRelation record);

    int updateByPrimaryKey(PostLabelRelation record);
}