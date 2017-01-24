package com.movision.mybatis.roleMenuRelation.entity;

import com.wordnik.swagger.annotations.ApiModel;

@ApiModel(value = "角色菜单关系", description = "角色菜单关系")
public class RoleMenuRelation {
    private Integer id;

    private Integer roleid;

    private Integer menuid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleid() {
        return roleid;
    }

    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }

    public Integer getMenuid() {
        return menuid;
    }

    public void setMenuid(Integer menuid) {
        this.menuid = menuid;
    }
}