package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.jobRelresume;

public interface jobRelresumeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(jobRelresume record);

    int insertSelective(jobRelresume record);

    jobRelresume selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(jobRelresume record);

    int updateByPrimaryKey(jobRelresume record);
}