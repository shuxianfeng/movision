//package com.movision.controller.boss;
//
//import com.movision.common.Response;
//import com.movision.facade.user.RoleMenuRelationFacade;
//import com.movision.mybatis.roleMenuRelation.entity.RoleMenuRelation;
//import com.wordnik.swagger.annotations.ApiOperation;
//import com.wordnik.swagger.annotations.ApiParam;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * @Author zhuangyuhao
// * @Date 2017/1/24 17:19
// */
//@RestController
//@RequestMapping("boss/role_menu_relation")
//public class RoleMenuRelationController {
//    @Autowired
//    private RoleMenuRelationFacade roleMenuRelationFacade;
//
//    /*@ApiOperation(value = "新增权限菜单关系", notes = "新增权限菜单关系", response = Response.class)
//    @RequestMapping(value = "add_role_menu_relation", method = RequestMethod.POST)
//    public Response addRoleMenuRelation(@ApiParam @ModelAttribute RoleMenuRelation roleMenuRelation) {
//        Response response = new Response();
//        boolean isAdd = roleMenuRelationFacade.addRoleMenuRelation(roleMenuRelation);
//        if (isAdd) {
//            response.setCode(200);
//        } else {
//            response.setCode(400);
//        }
//        return response;
//    }*/
//
//    /*@ApiOperation(value = "批量删除权限菜单关系", notes = "批量删除权限菜单关系", response = Response.class)
//    @RequestMapping(value = "batch_delete", method = RequestMethod.POST)
//    public Response batchDelete(@ApiParam(value = "权限菜单关系表的id,以逗号分隔") @RequestParam String ids) {
//        Response response = new Response();
//        String[] idList = ids.split(",");
//
//        List<String> failDeleteIdList = roleMenuRelationFacade.batchDelete(idList);
//        response.setCode(200);
//        response.setData(failDeleteIdList);
//        return response;
//    }*/
//
//
//}
