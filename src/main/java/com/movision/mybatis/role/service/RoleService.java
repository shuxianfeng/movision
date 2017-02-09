package com.movision.mybatis.role.service;


import com.movision.mybatis.bossMenu.entity.Menu;
import com.movision.mybatis.role.entity.Role;
import com.movision.mybatis.role.mapper.RoleMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    public List<Role> queryRoleList(Paging<Role> pager, Map<String, Object> map) {
        try {
            log.info("查询角色列表");
            return roleMapper.selectRoleList(pager.getRowBounds(), map);
        } catch (Exception e) {
            log.error("查询角色列表异常", e);
            throw e;
        }
    }

    public List<Role> queryRoleComboList() {
        try {
            log.info("查询角色下拉列表");
            return roleMapper.selectRoleComboList();
        } catch (Exception e) {
            log.error("查询角色下拉列表异常", e);
            throw e;
        }
    }

    public Role selectByPrimaryKey(Integer id) {
        try {
            log.info("根据id查询角色信息, id=" + id);
            return roleMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            log.error("根据id查询角色信息失败，id=" + id);
            throw e;
        }
    }


}
