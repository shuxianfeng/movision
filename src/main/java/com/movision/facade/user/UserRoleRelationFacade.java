package com.movision.facade.user;

import com.movision.mybatis.userRoleRelation.entity.UserRoleRelation;
import com.movision.mybatis.userRoleRelation.service.UserRoleRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/24 19:38
 */
@Service
public class UserRoleRelationFacade {
    @Autowired
    private UserRoleRelationService userRoleRelationService;

    public Boolean addRelation(UserRoleRelation userRoleRelation) {
        return userRoleRelationService.addRelation(userRoleRelation);
    }

    public int getRoleidByUserid(int userid) {
        return userRoleRelationService.getRoleidByUserid(userid);
    }
}
