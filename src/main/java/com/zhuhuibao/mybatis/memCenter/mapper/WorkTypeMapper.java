package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.UnitType;
import com.zhuhuibao.mybatis.memCenter.entity.WorkType;

import java.util.List;

public interface WorkTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WorkType record);

    int insertSelective(WorkType record);

    WorkType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkType record);

    int updateByPrimaryKey(WorkType record);

    List<WorkType> findWorkTypeList();
}