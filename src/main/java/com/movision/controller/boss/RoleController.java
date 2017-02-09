package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.user.RoleFacade;
import com.movision.facade.user.RoleMenuRelationFacade;
import com.movision.facade.user.UserRoleRelationFacade;
import com.movision.mybatis.role.entity.Role;
import com.movision.utils.CommonUtils;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/24 14:08
 */
@RestController
@RequestMapping("boss/role")
public class RoleController {

    @Autowired
    private RoleFacade roleFacade;

    @Autowired
    private UserRoleRelationFacade userRoleRelationFacade;

    @Autowired
    private RoleMenuRelationFacade roleMenuRelationFacade;


    @ApiOperation(value = "新增角色", notes = "新增角色", response = Response.class)
    @RequestMapping(value = "add_role", method = RequestMethod.POST)
    public Response addRole(@ApiParam @ModelAttribute Role role) {
        Response response = new Response();
        boolean isAdd = roleFacade.addUserRole(role);
        if (isAdd) {
            response.setCode(200);
        } else {
            response.setCode(400);
        }
        return response;
    }

    @ApiOperation(value = "删除角色", notes = "删除角色", response = Response.class)
    @RequestMapping(value = "del_role", method = RequestMethod.POST)
    public Response delRole(@ApiParam(value = "角色id,以逗号分隔") @RequestParam String roleids) {
        Response response = new Response();
        int[] roleidArray = CommonUtils.idsStringToIntArray(roleids);
        //1 删除该角色用户关系
        userRoleRelationFacade.deleteRelationsByRoleid(roleidArray);

        //2 删除该角色菜单关系
        roleMenuRelationFacade.delRelationByRoleid(roleidArray);

        //3 删除该角色
        roleFacade.delRoles(roleidArray);

        response.setCode(200);
        return response;
    }

    @ApiOperation(value = "角色列表", notes = "角色列表", response = Response.class)
    @RequestMapping(value = "role_list", method = RequestMethod.GET)
    public Response getRoleList(@RequestParam(required = false) String pageNo,
                                @RequestParam(required = false) String pageSize,
                                @ApiParam(value = "角色名称") @RequestParam(required = false) String rolename) {
        Response response = new Response();
        List<Role> list = roleFacade.queryRoleList(pageNo, pageSize, rolename);
        response.setData(list);
        return response;
    }

    @ApiOperation(value = "角色下拉列表", notes = "角色下拉列表", response = Response.class)
    @RequestMapping(value = "role_combo_list", method = RequestMethod.GET)
    public Response getRoleComboList() {

        Response response = new Response();
        List<Role> comboList = roleFacade.queryRoleComboList();
        response.setData(comboList);
        return response;
    }

}
