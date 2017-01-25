package com.movision.mybatis.userRoleRelation.entity;

import com.wordnik.swagger.annotations.ApiModel;

@ApiModel(value = "用户角色关系表", description = "用户角色关系表")
public class UserRoleRelation {
    private Integer id;

    private Integer userid;

    private Integer roleid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getRoleid() {
        return roleid;
    }

    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }
}