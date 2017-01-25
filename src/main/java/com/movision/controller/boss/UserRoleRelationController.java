package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.user.UserRoleRelationFacade;
import com.movision.mybatis.bossMenu.entity.Menu;
import com.movision.mybatis.userRoleRelation.entity.UserRoleRelation;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/24 19:40
 */
@RestController
@RequestMapping("boss/user_role_relation")
public class UserRoleRelationController {
    @Autowired
    private UserRoleRelationFacade userRoleRelationFacade;

    @ApiOperation(value = "新增用户角色关系", notes = "新增用户角色关系", response = Response.class)
    @RequestMapping(value = "add_user_role_relation", method = RequestMethod.POST)
    public Response addMenu(@ApiParam @ModelAttribute UserRoleRelation userRoleRelation) {
        Response response = new Response();
        boolean isAdd = userRoleRelationFacade.addRelation(userRoleRelation);
        if (isAdd) {
            response.setCode(200);
        } else {
            response.setCode(400);
        }
        return response;
    }


}
