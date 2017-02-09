package com.movision.mybatis.userRoleRelation.mapper;

import com.movision.mybatis.userRoleRelation.entity.UserRoleRelation;

public interface UserRoleRelationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserRoleRelation record);

    int insertSelective(UserRoleRelation record);

    UserRoleRelation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserRoleRelation record);

    int updateByPrimaryKey(UserRoleRelation record);

    int selectRoleidByUserid(Integer userid);

    void deleteRelationsbyUserid(int[] userids);

    void deleteRelationsbyRoleid(int[] roleids);

    int updateByUserid(UserRoleRelation relation);

}