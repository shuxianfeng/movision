package com.zhuhuibao.mybatis.advertising.mapper;

import com.zhuhuibao.mybatis.advertising.entity.SysAdvAttr;

public interface SysAdvAttrMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAdvAttr record);

    int insertSelective(SysAdvAttr record);

    SysAdvAttr selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAdvAttr record);

    int updateByPrimaryKey(SysAdvAttr record);
}