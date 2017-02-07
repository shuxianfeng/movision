package com.movision.mybatis.rewarded.mapper;

import com.movision.mybatis.rewarded.entity.Rewarded;

import java.util.Map;

public interface RewardedMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Rewarded record);

    int insertSelective(Rewarded record);

    Rewarded selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Rewarded record);

    int updateByPrimaryKey(Rewarded record);

    Integer queryRewardedBySum(Integer postid);

}