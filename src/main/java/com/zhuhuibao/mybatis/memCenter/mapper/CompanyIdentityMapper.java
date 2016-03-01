package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.CompanyIdentity;

import java.util.List;

public interface CompanyIdentityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CompanyIdentity record);

    int insertSelective(CompanyIdentity record);

    CompanyIdentity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CompanyIdentity record);

    int updateByPrimaryKey(CompanyIdentity record);

    List<CompanyIdentity> findCompanyIdentityList();
}