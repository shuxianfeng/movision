package com.movision.facade.user;

import com.movision.mybatis.roleMenuRelation.entity.RoleMenuRelation;
import com.movision.mybatis.roleMenuRelation.service.RoleMenuRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/22 10:27
 */
@Service
public class RoleMenuRelationFacade {

    @Autowired
    private RoleMenuRelationService roleMenuRelationService;

    public Boolean addRoleMenuRelation(RoleMenuRelation roleMenuRelation) {
        return roleMenuRelationService.addRoleMenuRelation(roleMenuRelation);
    }

    public List<String> batchDelete(String[] ids) {
        return roleMenuRelationService.batchDelete(ids);
    }
}
