package com.movision.mybatis.bossMenu.entity;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/14 17:02
 */
public class MenuDetail {
    private Integer id;
    private String menuname;

    private Integer pid;

    private Integer orderid;

    private Integer isdel;

    private String remark;

    private String url;

    private String parentMenuName;

    private Integer isshow;

    public void setIsshow(Integer isshow) {
        this.isshow = isshow;
    }

    public Integer getIsshow() {

        return isshow;
    }

    @Override
    public String toString() {
        return "MenuDetail{" +
                "id=" + id +
                ", menuname='" + menuname + '\'' +
                ", pid=" + pid +
                ", orderid=" + orderid +
                ", isdel=" + isdel +
                ", remark='" + remark + '\'' +
                ", url='" + url + '\'' +
                ", parentMenuName='" + parentMenuName + '\'' +
                ", isshow=" + isshow +
                '}';
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

    public void setUrl(String url) {
        this.url = url;
    }

    public void setParentMenuName(String parentMenuName) {
        this.parentMenuName = parentMenuName;
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

    public String getUrl() {
        return url;
    }

    public String getParentMenuName() {
        return parentMenuName;
    }

    public MenuDetail(Integer id, String menuname, Integer pid, Integer orderid, Integer isdel, String remark, String url, String parentMenuName, Integer isshow) {
        this.id = id;
        this.menuname = menuname;
        this.pid = pid;
        this.orderid = orderid;
        this.isdel = isdel;
        this.remark = remark;
        this.url = url;
        this.parentMenuName = parentMenuName;
        this.isshow = isshow;
    }
}
