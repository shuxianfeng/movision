package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.MemRealCheck;

import java.util.Map;

public interface MemRealCheckMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MemRealCheck record);

    int insertSelective(MemRealCheck record);

    MemRealCheck selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MemRealCheck record);

    int updateByPrimaryKey(MemRealCheck record);

    Map<String,Object> findMemrealCheck(String id);
}