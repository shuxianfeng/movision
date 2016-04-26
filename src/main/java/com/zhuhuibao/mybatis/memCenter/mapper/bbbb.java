package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.aaa;

public interface bbbb {
    int deleteByPrimaryKey(Long id);

    int insert(aaa record);

    int insertSelective(aaa record);

    aaa selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(aaa record);

    int updateByPrimaryKey(aaa record);
}