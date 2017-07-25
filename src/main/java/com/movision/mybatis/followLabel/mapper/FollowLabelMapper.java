package com.movision.mybatis.followLabel.mapper;

import com.movision.mybatis.followLabel.entity.FollowLabel;

public interface FollowLabelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FollowLabel record);

    int insertSelective(FollowLabel record);

    FollowLabel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FollowLabel record);

    int updateByPrimaryKey(FollowLabel record);
}