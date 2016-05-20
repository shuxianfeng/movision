package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.JobRelResume;

import java.util.Map;

public interface JobRelResumeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(JobRelResume record);

    int insertSelective(JobRelResume record);

    JobRelResume selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(JobRelResume record);

    int updateByPrimaryKey(JobRelResume record);

    Integer isExistApplyPosition(Map<String,Object> map);
}