package com.movision.mybatis.bossMenu.entity;


import java.io.Serializable;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/13 16:22
 */
public class AuthMenu implements Serializable {
    private Integer id;
    private String menuname;

    private Integer pid;

    private Integer orderid;

    private Integer isdel;

    private String remark;
    private Boolean isAuthroize;

    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {

        return url;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setAuthroize(Boolean authroize) {
        isAuthroize = authroize;
    }

    public Integer getId() {

        return id;
    }

    public String getMenuname() {
        return menuname;
    }

    public Integer getPid() {
        return pid;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public String getRemark() {
        return remark;
    }

    public Boolean getAuthroize() {
        return isAuthroize != null;
    }

    @Override
    public String toString() {
        return "AuthMenu{" +
                "id=" + id +
                ", menuname='" + menuname + '\'' +
                ", pid=" + pid +
                ", orderid=" + orderid +
                ", isdel=" + isdel +
                ", remark='" + remark + '\'' +
                ", isAuthroize=" + isAuthroize +
                ", url='" + url + '\'' +
                '}';
    }
}
