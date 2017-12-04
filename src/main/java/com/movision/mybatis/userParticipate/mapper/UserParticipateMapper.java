package com.movision.mybatis.userParticipate.mapper;

import com.movision.mybatis.userParticipate.entity.UserParticipate;
import com.movision.mybatis.userParticipate.entity.UserParticipateVo;

import java.util.List;
import java.util.Map;

public interface UserParticipateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserParticipate record);

    int insertSelective(UserParticipate record);

    UserParticipate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserParticipate record);

    int updateByPrimaryKey(UserParticipate record);

    List<UserParticipateVo> queryPostStatistics(Map map);

    UserParticipateVo queryPostStatisticsGather(Map map);
}