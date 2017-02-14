package com.movision.mybatis.roleMenuRelation.mapper;

import com.movision.mybatis.roleMenuRelation.entity.RoleMenuRelation;

import java.util.Map;

public interface RoleMenuRelationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleMenuRelation record);

    int insertSelective(RoleMenuRelation record);

    RoleMenuRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleMenuRelation record);

    int updateByPrimaryKey(RoleMenuRelation record);

    void delRelationByRoleid(int[] roleid);

    void batchAddByMenuid(Map<String, Object> map);
}