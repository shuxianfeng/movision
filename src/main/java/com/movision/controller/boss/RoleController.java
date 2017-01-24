package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.user.RoleFacade;
import com.movision.mybatis.role.entity.Role;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/24 14:08
 */
@RestController
@RequestMapping("boss/role")
public class RoleController {

    @Autowired
    private RoleFacade roleFacade;


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
