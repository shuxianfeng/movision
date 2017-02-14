package com.movision.mybatis.roleMenuRelation.service;

import com.movision.mybatis.roleMenuRelation.entity.RoleMenuRelation;

import com.movision.mybatis.roleMenuRelation.mapper.RoleMenuRelationMapper;
import com.movision.utils.ListUtil;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/22 10:18
 */
@Service
@Transactional
public class RoleMenuRelationService {
    private static Logger log = LoggerFactory.getLogger(RoleMenuRelationService.class);

    @Autowired
    private RoleMenuRelationMapper roleMenuRelationMapper;


    public Boolean addRoleMenuRelation(RoleMenuRelation roleMenuRelation) {
        try {
            log.info("新增角色权限关系，roleMenuRelation = " + roleMenuRelation.toString());
            int n = roleMenuRelationMapper.insertSelective(roleMenuRelation);
            return n == 1;
        } catch (Exception e) {
            log.error("新增角色权限关系失败， roleMenuRelation =" + roleMenuRelation.toString());
            throw e;
        }
    }


    public Boolean delete(String id) {
        try {
            log.info("删除角色权限关系，id = " + id);
            int n = roleMenuRelationMapper.deleteByPrimaryKey(Integer.valueOf(id));
            return n == 1;
        } catch (Exception e) {
            log.error("删除角色权限关系失败， id =" + id);
            throw e;
        }
    }

    public List<String> batchDelete(String[] ids) {
        List<String> errorDeleteIdList = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            if (!this.delete(ids[i])) {
                errorDeleteIdList.add(ids[i]);
            }
        }
        if (ListUtil.isNotEmpty(errorDeleteIdList)) {
            log.info("总共有" + errorDeleteIdList.size() + "个数据在删除时出现异常； 具体的id如下：" + errorDeleteIdList.toString());
        }
        return errorDeleteIdList;
    }

    public void delRelationByRoleid(int[] roleid) {
        roleMenuRelationMapper.delRelationByRoleid(roleid);
    }


    public void batchAddByMenuid(Map map) {
        try {
            log.info("批量增加菜单和权限关系");
            roleMenuRelationMapper.batchAddByMenuid(map);
        } catch (Exception e) {
            log.error("批量增加菜单和权限关系失败");
            throw e;
        }
    }


}
