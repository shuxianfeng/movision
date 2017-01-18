package com.movision.mybatis.adminUser.mapper;

import com.movision.mybatis.adminUser.entity.AdminUser;

public interface AdminUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdminUser record);

    int insertSelective(AdminUser record);

    AdminUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdminUser record);

    int updateByPrimaryKey(AdminUser record);

    AdminUser selectByPhone(AdminUser adminUser);
}