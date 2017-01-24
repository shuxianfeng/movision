package com.movision.mybatis.submission.mapper;

import com.movision.mybatis.submission.entity.Submission;

import java.util.Map;

public interface SubmissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Submission record);

    int insertSelective(Submission record);

    Submission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Submission record);

    int updateByPrimaryKey(Submission record);

    int commitSubmission(Map<String, Object> parammap);
}