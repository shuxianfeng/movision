package com.movision.mybatis.role.service;


import com.movision.mybatis.role.entity.Role;
import com.movision.mybatis.role.mapper.RoleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/22 9:44
 */
@Service
public class RoleService {

    private static Logger log = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    private RoleMapper roleMapper;

    public Boolean addUserRole(Role role) {
        try {
            log.info("新增角色，role =" + role.toString());
            int n = roleMapper.insertSelective(role);
            return n == 1;
        } catch (Exception e) {
            log.error("新增角色失败，role = " + role.toString());
            throw e;
        }
    }

    public void delRoles(int[] ids) {
        try {
            log.info("删除角色，ids =" + ids);
            roleMapper.delRoles(ids);
        } catch (Exception e) {
            log.error("删除角色失败，ids = " + ids);
            throw e;
        }
    }


}
