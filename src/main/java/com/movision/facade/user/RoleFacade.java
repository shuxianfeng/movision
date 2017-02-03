package com.movision.facade.user;

import com.movision.mybatis.role.entity.Role;
import com.movision.mybatis.role.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/22 9:55
 */
@Service
public class RoleFacade {
    private static Logger logger = LoggerFactory.getLogger(RoleFacade.class);

    @Autowired
    private RoleService roleService;

    public Boolean addUserRole(Role role) {
        return roleService.addUserRole(role);
    }

    public void delRoles(int[] ids) {
        roleService.delRoles(ids);
    }
}
