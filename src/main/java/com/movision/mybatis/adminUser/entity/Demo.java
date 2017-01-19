package com.movision.mybatis.adminUser.entity;

import com.movision.mybatis.post.entity.Post;

import java.util.Date;
import java.util.List;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/19 11:21
 */
public class Demo {
    private Integer id;

    private String name;

    private String phone;

    private String username;

    private String password;

    private Integer issuper;

    private Integer status;

    private Integer isdel;

    private Date createtime;

    private Date afterlogintime;

    private Date beforelogintime;

    @Override
    public String toString() {
        return "Demo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", issuper=" + issuper +
                ", status=" + status +
                ", isdel=" + isdel +
                ", createtime=" + createtime +
                ", afterlogintime=" + afterlogintime +
                ", beforelogintime=" + beforelogintime +
                ", postList=" + postList +
                '}';
    }

    private List<Post> postList;

    public Demo(Integer id, String name, String phone, String username, String password, Integer issuper, Integer status, Integer isdel, Date createtime, Date afterlogintime, Date beforelogintime, List<Post> postList) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.issuper = issuper;
        this.status = status;
        this.isdel = isdel;
        this.createtime = createtime;
        this.afterlogintime = afterlogintime;
        this.beforelogintime = beforelogintime;
        this.postList = postList;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIssuper(Integer issuper) {
        this.issuper = issuper;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public void setAfterlogintime(Date afterlogintime) {
        this.afterlogintime = afterlogintime;
    }

    public void setBeforelogintime(Date beforelogintime) {
        this.beforelogintime = beforelogintime;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Integer getIssuper() {
        return issuper;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public Date getAfterlogintime() {
        return afterlogintime;
    }

    public Date getBeforelogintime() {
        return beforelogintime;
    }

    public List<Post> getPostList() {
        return postList;
    }

}
