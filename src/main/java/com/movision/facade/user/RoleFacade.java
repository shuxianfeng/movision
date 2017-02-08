package com.movision.facade.user;

import com.movision.mybatis.bossMenu.entity.Menu;
import com.movision.mybatis.role.entity.Role;
import com.movision.mybatis.role.service.RoleService;
import com.movision.utils.pagination.model.Paging;
import com.movision.utils.pagination.util.StringUtils;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Boolean addUserRole(Role role) {
        return roleService.addUserRole(role);
    }

    public void delRoles(int[] ids) {
        roleService.delRoles(ids);
    }

    public List<Role> queryRoleList(String pageNo, String pageSize, String rolename) {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Role> pager = new Paging<Role>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashedMap();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(rolename)) {
            map.put("rolename", rolename);
        }
        return roleService.queryRoleList(pager, map);
    }
}
