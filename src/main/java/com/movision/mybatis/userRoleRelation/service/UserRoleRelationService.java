package com.movision.mybatis.userRoleRelation.service;

import com.movision.mybatis.userRoleRelation.entity.UserRoleRelation;
import com.movision.mybatis.userRoleRelation.mapper.UserRoleRelationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/24 19:30
 */
@Service
@Transactional
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

    public Integer getRoleidByUserid(int userid) {
        try {
            log.info("查询条件，userid = " + userid);
            Integer roleid = userRoleRelationMapper.selectRoleidByUserid(userid);
            log.info("查询结果,roleid = " + roleid);
            return roleid;
        } catch (Exception e) {
            log.error("根据用户id查询角色id失败，userid = " + userid, e);
            throw e;
        }
    }

    public void deleteRelationsByUserid(int[] userids) {
        userRoleRelationMapper.deleteRelationsbyUserid(userids);
    }

    public void deleteRelationsByRoleid(int[] roleids) {
        userRoleRelationMapper.deleteRelationsbyRoleid(roleids);
    }

    public Boolean updateByUserid(UserRoleRelation userRoleRelation) {
        try {
            log.info("修改用户角色关系， userid = " + userRoleRelation.getUserid().toString());
            int n = userRoleRelationMapper.updateByUserid(userRoleRelation);
            return n == 1;
        } catch (Exception e) {
            log.error("修改用户角色关系失败, userid = " + userRoleRelation.getUserid().toString(), e);
            throw e;
        }
    }

}
