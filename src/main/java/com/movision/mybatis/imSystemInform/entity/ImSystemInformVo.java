package com.movision.mybatis.imSystemInform.entity;

import java.io.Serializable;
import java.util.Date;

public class ImSystemInformVo implements Serializable {
    private Integer id;

    private Integer userid;

    private String fromAccid;

    private String toAccids;

    private String body;

    private Date informTime;

    private String title;

    private String pushcontent;

    private String informidentity;  //系统通知唯一标示

    private Integer isread;//是否已读 0否 1是

    private String coverimg;//运营通知封面图

    private Integer isoperation;//代表是否为运营通知

    private Integer activeid;

    public Integer getActiveid() {
        return activeid;
    }

    public void setActiveid(Integer activeid) {
        this.activeid = activeid;
    }

    public Integer getIsoperation() {
        return isoperation;
    }

    public void setIsoperation(Integer isoperation) {
        this.isoperation = isoperation;
    }

    public String getCoverimg() {
        return coverimg;
    }

    public void setCoverimg(String coverimg) {
        this.coverimg = coverimg;
    }

    public Integer getIsread() {
        return isread;
    }

    public void setIsread(Integer isread) {
        this.isread = isread;
    }

    public String getInformidentity() {
        return informidentity;
    }

    public void setInformidentity(String informidentity) {
        this.informidentity = informidentity;
    }

    public String getPushcontent() {
        return pushcontent;
    }

    public void setPushcontent(String pushcontent) {
        this.pushcontent = pushcontent;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {

        return title;
    }

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

    public String getFromAccid() {
        return fromAccid;
    }

    public void setFromAccid(String fromAccid) {
        this.fromAccid = fromAccid == null ? null : fromAccid.trim();
    }

    public String getToAccids() {
        return toAccids;
    }

    public void setToAccids(String toAccids) {
        this.toAccids = toAccids == null ? null : toAccids.trim();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body == null ? null : body.trim();
    }

    public Date getInformTime() {
        return informTime;
    }

    public void setInformTime(Date informTime) {
        this.informTime = informTime;
    }

    /**
     * 重写equals方法，用于比对
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof ImSystemInformVo && this.informidentity != null && this.informidentity.equals(((ImSystemInformVo) obj).getInformidentity());
    }
}