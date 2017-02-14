package com.movision.mybatis.bossMenu.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "菜单", description = "菜单")
public class Menu {
    private Integer id;
    @ApiModelProperty(value = "菜单名称", required = true)
    private String menuname;

    private Integer pid;

    private Integer orderid;

    private Integer isdel;

    private String remark;

    @ApiModelProperty(value = "菜单的rest接口路径")
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {

        return url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname == null ? null : menuname.trim();
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", menuname='" + menuname + '\'' +
                ", pid=" + pid +
                ", orderid=" + orderid +
                ", isdel=" + isdel +
                ", remark='" + remark + '\'' +
                '}';
    }
}