package com.movision.mybatis.submission.mapper;

import com.movision.mybatis.submission.entity.Submission;
import com.movision.mybatis.submission.entity.SubmissionVo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface SubmissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Submission record);

    int insertSelective(Submission record);

    Submission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Submission record);

    int updateByPrimaryKey(Submission record);

    int commitSubmission(Map<String, Object> parammap);

    List<SubmissionVo> findAllQueryContributeList(RowBounds rowBounds);

    List<SubmissionVo> findAllqueryUniteConditionByContribute(Map map, RowBounds rowBounds);
}