package com.movision.mybatis.adminRoleMenu.mapper;

import com.movision.mybatis.adminRoleMenu.entity.AdminRoleMenu;

public interface AdminRoleMenuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdminRoleMenu record);

    int insertSelective(AdminRoleMenu record);

    AdminRoleMenu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdminRoleMenu record);

    int updateByPrimaryKey(AdminRoleMenu record);
}