package com.movision.mybatis.manageType.mapper;

import com.movision.mybatis.manageType.entity.ManageType;

import java.util.List;

public interface ManageTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ManageType record);

    int insertSelective(ManageType record);

    ManageType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManageType record);

    int updateByPrimaryKey(ManageType record);

    List<ManageType> queryAdvertisementTypeList();
}