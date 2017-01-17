package com.movision.mybatis.adminUser.entity;

import java.util.Date;

public class AdminUser {
    private Integer id;

    private String name;

    private Integer phone;

    private String username;

    private String password;

    private Integer issuper;

    private Integer status;

    private Integer isdel;

    private Date createtime;

    private Date afterlogintime;

    private Date beforelogintime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getIssuper() {
        return issuper;
    }

    public void setIssuper(Integer issuper) {
        this.issuper = issuper;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getAfterlogintime() {
        return afterlogintime;
    }

    public void setAfterlogintime(Date afterlogintime) {
        this.afterlogintime = afterlogintime;
    }

    public Date getBeforelogintime() {
        return beforelogintime;
    }

    public void setBeforelogintime(Date beforelogintime) {
        this.beforelogintime = beforelogintime;
    }
}