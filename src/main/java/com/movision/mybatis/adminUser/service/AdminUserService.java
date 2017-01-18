package com.movision.mybatis.adminUser.service;

import com.movision.mybatis.adminUser.entity.AdminUser;
import com.movision.mybatis.adminUser.mapper.AdminUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/18 18:32
 */
@Service
public class AdminUserService {

    @Autowired
    private AdminUserMapper adminUserMapper;

    public AdminUser queryAdminUserByPhone(String phone) {
        AdminUser adminUser = new AdminUser();
        adminUser.setPhone(phone);
        return adminUserMapper.selectByPhone(adminUser);
    }

}
