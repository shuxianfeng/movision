package com.zhuhuibao.mybatis.activity.entity;

import java.util.Date;

public class Activity {
    private Long id;

    private String activityName;

    private Date activityStarttime;

    private Date activityEndtime;

    private Date applyEndtime;

    private String organized;

    private String coOrganized;

    private String applyFee;

    private String activityAddress;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName == null ? null : activityName.trim();
    }

    public Date getActivityStarttime() {
        return activityStarttime;
    }

    public void setActivityStarttime(Date activityStarttime) {
        this.activityStarttime = activityStarttime;
    }

    public Date getActivityEndtime() {
        return activityEndtime;
    }

    public void setActivityEndtime(Date activityEndtime) {
        this.activityEndtime = activityEndtime;
    }

    public Date getApplyEndtime() {
        return applyEndtime;
    }

    public void setApplyEndtime(Date applyEndtime) {
        this.applyEndtime = applyEndtime;
    }

    public String getOrganized() {
        return organized;
    }

    public void setOrganized(String organized) {
        this.organized = organized == null ? null : organized.trim();
    }

    public String getCoOrganized() {
        return coOrganized;
    }

    public void setCoOrganized(String coOrganized) {
        this.coOrganized = coOrganized == null ? null : coOrganized.trim();
    }

    public String getApplyFee() {
        return applyFee;
    }

    public void setApplyFee(String applyFee) {
        this.applyFee = applyFee == null ? null : applyFee.trim();
    }

    public String getActivityAddress() {
        return activityAddress;
    }

    public void setActivityAddress(String activityAddress) {
        this.activityAddress = activityAddress == null ? null : activityAddress.trim();
    }

}