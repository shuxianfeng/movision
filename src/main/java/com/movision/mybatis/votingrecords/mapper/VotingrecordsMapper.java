package com.movision.mybatis.votingrecords.mapper;

import com.movision.mybatis.votingrecords.entity.Votingrecords;

public interface VotingrecordsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Votingrecords record);

    int insertSelective(Votingrecords record);

    Votingrecords selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Votingrecords record);

    int updateByPrimaryKey(Votingrecords record);
}