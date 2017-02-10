package com.movision.facade.user;

import com.movision.mybatis.bossMenu.entity.Menu;
import com.movision.mybatis.bossMenu.service.MenuService;
import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.mybatis.bossUser.service.BossUserService;
import com.movision.mybatis.role.entity.Role;
import com.movision.mybatis.role.service.RoleService;
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
    private static Logger logger = LoggerFactory.getLogger(RoleFacade.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private BossUserService bossUserService;

    public Boolean addUserRole(Role role) {
        return roleService.addUserRole(role);
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

}
