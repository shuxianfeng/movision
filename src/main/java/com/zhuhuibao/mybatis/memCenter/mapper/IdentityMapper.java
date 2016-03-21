package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.Identity;

import java.util.List;

public interface IdentityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Identity record);

    int insertSelective(Identity record);

    Identity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Identity record);

    int updateByPrimaryKey(Identity record);

    List<Identity> findIdentityList();
}