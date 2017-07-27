package com.movision.mybatis.followUser.mapper;

import com.movision.mybatis.followUser.entity.FollowUser;

import java.util.Map;

public interface FollowUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FollowUser record);

    int insertSelective(FollowUser record);

    FollowUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FollowUser record);

    int updateByPrimaryKey(FollowUser record);

    int yesOrNo(Map map);
}