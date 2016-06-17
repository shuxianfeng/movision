package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.EnterpriseType;

import java.util.List;

public interface EnterpriseTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EnterpriseType record);

    int insertSelective(EnterpriseType record);

    EnterpriseType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EnterpriseType record);

    int updateByPrimaryKey(EnterpriseType record);

    List<EnterpriseType> findEnterpriseTypeList();
}