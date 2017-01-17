package com.movision.mybatis.adminUserRole.mapper;

import com.movision.mybatis.adminUserRole.entity.AdminUserRole;

public interface AdminUserRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdminUserRole record);

    int insertSelective(AdminUserRole record);

    AdminUserRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdminUserRole record);

    int updateByPrimaryKey(AdminUserRole record);
}