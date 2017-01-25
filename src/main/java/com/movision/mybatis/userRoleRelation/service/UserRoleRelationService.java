package com.movision.mybatis.userRoleRelation.service;

import com.movision.mybatis.userRoleRelation.entity.UserRoleRelation;
import com.movision.mybatis.userRoleRelation.mapper.UserRoleRelationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/24 19:30
 */
@Service
public class UserRoleRelationService {
    private static Logger log = LoggerFactory.getLogger(UserRoleRelationService.class);

    @Autowired
    private UserRoleRelationMapper userRoleRelationMapper;

    public Boolean addRelation(UserRoleRelation userRoleRelation) {
        try {
            log.info("新增用户角色关系， userRoleRelation = " + userRoleRelation.toString());
            int n = userRoleRelationMapper.insertSelective(userRoleRelation);
            return n == 1;
        } catch (Exception e) {
            log.error("新增用户角色关系失败, bossUser = " + userRoleRelation.toString(), e);
            throw e;
        }
    }

    public int getRoleidByUserid(int userid) {
        try {
            log.info("查询条件，userid = " + userid);
            int roleid = userRoleRelationMapper.selectRoleidByUserid(userid);
            log.info("查询结果,roleid = " + roleid);
            return roleid;
        } catch (Exception e) {
            log.error("根据用户id查询角色id失败，userid = " + userid);
            throw e;
        }
    }

}
