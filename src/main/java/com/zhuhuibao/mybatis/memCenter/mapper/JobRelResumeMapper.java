package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.JobRelResume;

public interface JobRelResumeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(JobRelResume record);

    int insertSelective(JobRelResume record);

    JobRelResume selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(JobRelResume record);

    int updateByPrimaryKey(JobRelResume record);
}