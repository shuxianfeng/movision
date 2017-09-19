package com.movision.mybatis.imSystemInform.entity;

import java.io.Serializable;
import java.util.Date;

public class ImSystemInform implements Serializable {
    private Integer id;

    private Integer userid;

    private String fromAccid;

    private String toAccids;

    private String body;    //消息内容

    private Date informTime;

    private String title;

    private String pushcontent; //推送内容

    private String informidentity;  //系统通知的唯一标示

    private String coverimg;//运营通知封面图

    private Integer activeid;//活动id

    public Integer getActiveid() {
        return activeid;
    }

    public void setActiveid(Integer activeid) {
        this.activeid = activeid;
    }

    public String getCoverimg() {
        return coverimg;
    }

    public void setCoverimg(String coverimg) {
        this.coverimg = coverimg;
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
}