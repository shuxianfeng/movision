package com.movision.facade.user;

import com.movision.mybatis.bossMenu.entity.Menu;
import com.movision.mybatis.bossMenu.service.MenuService;
import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.mybatis.bossUser.service.BossUserService;
import com.movision.mybatis.role.entity.Role;
import com.movision.mybatis.role.service.RoleService;
import com.movision.mybatis.roleMenuRelation.service.RoleMenuRelationService;
import com.movision.utils.pagination.model.Paging;
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
 * @Date 2017/1/22 9:55
 */
@Service
public class RoleFacade {

    private static Logger log = LoggerFactory.getLogger(RoleFacade.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private BossUserService bossUserService;

    @Autowired
    private RoleMenuRelationService roleMenuRelationService;

    public int addUserRole(String remark, String name) {
        Role role = new Role();
        role.setRemark(remark);
        role.setRolename(name);
        roleService.addUserRole(role);
        return role.getId();
    }

    public void updateRole(String remark, String name, int id) {
        Role role = new Role();
        role.setId(id);
        role.setRemark(remark);
        role.setRolename(name);
        roleService.updateRole(role);
    }

    public void delRoles(int[] ids) {
        roleService.delRoles(ids);
    }

    public List<Role> queryRoleList(Paging<Role> pager, String rolename) {

        Map<String, Object> map = new HashedMap();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(rolename)) {
            map.put("rolename", rolename);
        }
        return roleService.queryRoleList(pager, map);
    }

    public List<Role> queryNotSuperAdminRoleComboList() {
        return roleService.queryNotSuperAdminRoleComboList();
    }

    /**
     * 获取所有角色关联的菜单和用户信息
     *
     * @param roleidArray
     * @return
     */
    public List<Map<String, Object>> getAllRelativeInfo(int[] roleidArray) {

        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < roleidArray.length; i++) {
            //查出角色信息
            int roleid = roleidArray[i];
            Role role = roleService.selectByPrimaryKey(roleid);
            //查出该角色对应的菜单信息
            List<Menu> menuList = menuService.queryMenuListByRoleid(roleid);
            //查出该角色对应的用户信息
            List<BossUser> userList = bossUserService.selectBossUserListByRoleId(roleid);
            Map map = new HashedMap();
            map.put("roleid", roleid);
            map.put("menuList", menuList);
            map.put("userList", userList);

            list.add(map);
        }

        return list;
    }

    public void updateRoleMenuRelation(String menuids, int roleid) {
        //先删除roleid对应的菜单关系
        int roleidArray[] = new int[1];
        roleidArray[0] = roleid;
        roleMenuRelationService.delRelationByRoleid(roleidArray);
        //再添加roleid对应的菜单关系
        this.batchAddByMenuid(menuids, roleid);

    }

    public void addRoleMenuRealtion(String menuid, int roleid) {
        int roleidArray[] = new int[1];
        roleidArray[0] = roleid;

        this.batchAddByMenuid(menuid, roleid);
    }


    /**
     * 批量新增role和menu关系
     *
     * @param menuids 多个菜单id
     * @param roleid  一个role id
     */
    public void batchAddByMenuid(String menuids, int roleid) {
        Map map = new HashedMap();
        String[] arr = menuids.split(",");
        int len = arr.length;
        int[] intarr = new int[len];
        for (int i = 0; i < len; i++) {
            intarr[i] = Integer.valueOf(arr[i]);
        }
        map.put("roleid", roleid);
        map.put("menuids", intarr);
        roleMenuRelationService.batchAddByMenuid(map);
    }

}
