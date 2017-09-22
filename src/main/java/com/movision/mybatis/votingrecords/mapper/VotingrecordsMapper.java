package com.movision.mybatis.votingrecords.mapper;

import com.movision.mybatis.votingrecords.entity.Votingrecords;

import java.util.Map;

public interface VotingrecordsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Votingrecords record);

    int insertSelective(Votingrecords record);

    Votingrecords selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Votingrecords record);

    int updateByPrimaryKey(Votingrecords record);

    int queryHave(Map map);

    //查询活动是怎么投票的
    int activeHowToVote(int activeid);

}