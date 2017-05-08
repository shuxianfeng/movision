package com.movision.mybatis.submission.mapper;

import com.movision.mybatis.submission.entity.Submission;
import com.movision.mybatis.submission.entity.SubmissionVo;
import com.movision.mybatis.user.entity.UserVo;
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

    Submission queryContributeBounce(String id);

    List<SubmissionVo> findAllqueryUniteConditionByContribute(Map map, RowBounds rowBounds);

    int update_contribute_audit(Map map);

    int deleteContributeById(String id);
}