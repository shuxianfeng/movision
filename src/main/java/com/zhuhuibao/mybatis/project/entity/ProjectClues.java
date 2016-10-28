package com.zhuhuibao.mybatis.project.entity;

import java.util.Date;

/**
 * 项目线索信息
 *
 * @author changxinwei
 * @since  2016/10/27
 *
 */
public class ProjectClues {
    private Integer id;

    private String prjName;

    private String prjContents;

    private String contact;

    private String mark;

    private Integer status;

    private Integer operateId;

    private Date addTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrjName() {
        return prjName;
    }

    public void setPrjName(String prjName) {
        this.prjName = prjName;
    }

    public String getPrjContents() {
        return prjContents;
    }

    public void setPrjContents(String prjContents) {
        this.prjContents = prjContents;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOperateId() {
        return operateId;
    }

    public void setOperateId(Integer operateId) {
        this.operateId = operateId;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}