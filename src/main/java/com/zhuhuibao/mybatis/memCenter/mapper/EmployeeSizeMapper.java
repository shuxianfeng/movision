package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.EmployeeSize;

import java.util.List;

public interface EmployeeSizeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EmployeeSize record);

    int insertSelective(EmployeeSize record);

    EmployeeSize selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EmployeeSize record);

    int updateByPrimaryKey(EmployeeSize record);

    List<EmployeeSize> findEmployeeSizeList();
}