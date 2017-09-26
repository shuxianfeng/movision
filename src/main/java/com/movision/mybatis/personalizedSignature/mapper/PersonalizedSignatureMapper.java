package com.movision.mybatis.personalizedSignature.mapper;

import com.movision.mybatis.personalizedSignature.entity.PersonalizedSignature;

import java.util.List;

public interface PersonalizedSignatureMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PersonalizedSignature record);

    int insertSelective(PersonalizedSignature record);

    PersonalizedSignature selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PersonalizedSignature record);

    int updateByPrimaryKey(PersonalizedSignature record);

    List<PersonalizedSignature> queryRoboltSignature(Integer number);
}