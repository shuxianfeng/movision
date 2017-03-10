package com.movision.facade.user;

import com.movision.common.constant.MsgCodeConstant;
import com.movision.exception.BusinessException;
import com.movision.mybatis.role.entity.Role;
import com.movision.mybatis.role.service.RoleService;
import com.movision.utils.propertiesLoader.MsgPropertiesLoader;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/17 15:44
 */
public class RoleFacadeTest {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleFacade roleFacade;

    @Test
    public void addUserRole() throws Exception {
        Role role = new Role();
        role.setRolename("神奇的小飞");
        this.validateRoleNameExist(role);
        roleService.addUserRole(role);
    }

    private void validateRoleNameExist(Role role) {
        //检验角色名是否已经存在
        int isExist = roleService.isExistSameName(role);
        if (isExist >= 1) {
            System.out.println("error");
            throw new BusinessException(MsgCodeConstant.boss_role_name_is_exist, MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.boss_role_name_is_exist)));
        }
    }

/*
    @Test
    public void updateRole() throws Exception {

    }
*/

}