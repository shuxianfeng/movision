package com.movision.facade.user;

import com.movision.mybatis.bossMenu.entity.Menu;
import com.movision.mybatis.bossMenu.service.MenuService;
import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.mybatis.role.entity.Role;
import com.movision.mybatis.role.service.RoleService;
import com.movision.mybatis.roleMenuRelation.entity.RoleMenuRelation;
import com.movision.mybatis.roleMenuRelation.service.RoleMenuRelationService;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/22 10:27
 */
@Service
public class RoleMenuRelationFacade {

    private static Logger log = LoggerFactory.getLogger(RoleMenuRelationFacade.class);

    @Autowired
    private RoleMenuRelationService roleMenuRelationService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    public Boolean addRoleMenuRelation(RoleMenuRelation roleMenuRelation) {
        return roleMenuRelationService.addRoleMenuRelation(roleMenuRelation);
    }

    public List<String> batchDelete(String[] ids) {
        return roleMenuRelationService.batchDelete(ids);
    }

    public void delRelationByRoleid(int[] roleid) {
        roleMenuRelationService.delRelationByRoleid(roleid);
    }

    /**
     * 获取所有关联的菜单信息
     *
     * @param roleidArray
     * @return
     */
    public List<Map<String, Object>> getAllRelativeMenuList(int[] roleidArray) {

        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < roleidArray.length; i++) {
            //查出角色信息
            int roleid = roleidArray[i];
            Role role = roleService.selectByPrimaryKey(roleid);
            //查出该角色对应的菜单信息
            List<Menu> menuList = menuService.queryMenuListByRoleid(roleid);
            Map map = new HashedMap();
            map.put("roleid", roleid);
            map.put("menuList", menuList);
            list.add(map);
        }

        log.info("获取所有关联的菜单信息,list=" + list);

        return list;
    }
}
