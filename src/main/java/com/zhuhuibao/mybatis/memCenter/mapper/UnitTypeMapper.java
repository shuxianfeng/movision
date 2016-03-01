package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.UnitType;

import java.util.List;

public interface UnitTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UnitType record);

    int insertSelective(UnitType record);

    UnitType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UnitType record);

    int updateByPrimaryKey(UnitType record);

    List<UnitType> findUnitTypeList();
}