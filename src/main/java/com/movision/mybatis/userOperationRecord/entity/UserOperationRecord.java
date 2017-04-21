package com.movision.mybatis.userOperationRecord.entity;

public class UserOperationRecord {
    private Integer id;

    private Integer userid;

    private Integer isfollow;

    private Integer iscollect;

    private Integer iszan;

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

    public Integer getIsfollow() {
        return isfollow;
    }

    public void setIsfollow(Integer isfollow) {
        this.isfollow = isfollow;
    }

    public Integer getIscollect() {
        return iscollect;
    }

    public void setIscollect(Integer iscollect) {
        this.iscollect = iscollect;
    }

    public Integer getIszan() {
        return iszan;
    }

    public void setIszan(Integer iszan) {
        this.iszan = iszan;
    }
}