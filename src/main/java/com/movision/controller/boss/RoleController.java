package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.user.RoleFacade;
import com.movision.facade.user.RoleMenuRelationFacade;
import com.movision.facade.user.UserRoleRelationFacade;
import com.movision.mybatis.role.entity.Role;
import com.movision.utils.CommonUtils;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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




    @ApiOperation(value = "删除角色前的检查,获取角色的关联用户和菜单信息", notes = "删除角色前的检查,获取角色的关联用户和菜单信息", response = Response.class)
    @RequestMapping(value = "get_role_relative_info", method = RequestMethod.GET)
    public Response delRoleCheck(@ApiParam(value = "角色id,以逗号分隔") @RequestParam String roleids) {
        Response response = new Response();
        int[] roleidArray = CommonUtils.idsStringToIntArray(roleids);
        List<Map<String, Object>> list = roleFacade.getAllRelativeInfo(roleidArray);
        response.setData(list);
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

    @ApiOperation(value = "角色列表(分页)", notes = "角色列表(分页)", response = Response.class)
    @RequestMapping(value = "role_list", method = RequestMethod.GET)
    public Response getRoleList(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                @RequestParam(required = false, defaultValue = "10") String pageSize,
                                @ApiParam(value = "角色名称") @RequestParam(required = false) String rolename) {
        Response response = new Response();
        Paging<Role> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Role> list = roleFacade.queryRoleList(pager, rolename);
        pager.result(list);
        response.setData(pager);
        return response;
    }

    @ApiOperation(value = "非超级管理员的角色下拉列表", notes = "非超级管理员的角色下拉列表", response = Response.class)
    @RequestMapping(value = "role_combo_list", method = RequestMethod.GET)
    public Response getRoleComboList() {

        Response response = new Response();
        List<Role> comboList = roleFacade.queryNotSuperAdminRoleComboList();
        response.setData(comboList);
        return response;
    }

    @ApiOperation(value = "修改角色", notes = "修改角色", response = Response.class)
    @RequestMapping(value = "update_role_info", method = RequestMethod.GET)
    public Response updateRoleInfo(@ApiParam(value = "菜单id字符串，以逗号分隔") @RequestParam(required = true) String menuids,
                                   @ApiParam(value = "角色id") @RequestParam(required = true) int roleid,
                                   @ApiParam(value = "角色名称") @RequestParam(required = false) String name,
                                   @ApiParam(value = "备注") @RequestParam(required = false) String remark) {
        Response response = new Response();
        //先修改角色
        roleFacade.updateRole(remark, name, roleid);
        //再修改角色的权限
        roleFacade.updateRoleMenuRelation(menuids, roleid);

        return response;
    }

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

}
