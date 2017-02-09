package com.movision.facade.user;

import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.mybatis.bossUser.service.BossUserService;
import com.movision.mybatis.role.entity.Role;
import com.movision.mybatis.role.service.RoleService;
import com.movision.mybatis.userRoleRelation.entity.UserRoleRelation;
import com.movision.mybatis.userRoleRelation.service.UserRoleRelationService;
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
 * @Date 2017/1/24 19:38
 */
@Service
public class UserRoleRelationFacade {

    private static Logger log = LoggerFactory.getLogger(UserRoleRelationFacade.class);

    @Autowired
    private UserRoleRelationService userRoleRelationService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BossUserService bossUserService;


    public Boolean addRelation(UserRoleRelation userRoleRelation) {
        return userRoleRelationService.addRelation(userRoleRelation);
    }

    public int getRoleidByUserid(int userid) {
        return userRoleRelationService.getRoleidByUserid(userid);
    }

    public void deleteRelationsByUserid(int[] userid) {
        userRoleRelationService.deleteRelationsByUserid(userid);
    }

    public void deleteRelationsByRoleid(int[] roleid) {
        userRoleRelationService.deleteRelationsByRoleid(roleid);
    }

    public Boolean updateByUserid(UserRoleRelation userRoleRelation) {
        return userRoleRelationService.updateByUserid(userRoleRelation);
    }

    /**
     * 获取所有关联的用户信息
     *
     * @param roleidArray
     * @return
     */
    public List<Map<String, Object>> getAllRelativeUserList(int[] roleidArray) {

        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < roleidArray.length; i++) {
            //查出角色信息
            int roleid = roleidArray[i];
            Role role = roleService.selectByPrimaryKey(roleid);
            //查出该角色对应的用户信息
            List<BossUser> userList = bossUserService.selectBossUserListByRoleId(roleid);
            Map map = new HashedMap();
            map.put("roleid", roleid);
            map.put("userlist", userList);
            list.add(map);
        }
        log.info("获取所有关联的用户信息,list=" + list);

        return list;
    }
}
