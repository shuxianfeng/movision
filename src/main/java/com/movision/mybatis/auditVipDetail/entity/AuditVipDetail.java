package com.movision.mybatis.auditVipDetail.entity;

import java.util.Date;

public class AuditVipDetail {
    private Integer id;

    private Integer applyId;

    private Integer userid;

    private Integer applyUserid;

    private Integer status;

    private Date auditTime;

    private String reason;//审核未通过的理由

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getApplyUserid() {
        return applyUserid;
    }

    public void setApplyUserid(Integer applyUserid) {
        this.applyUserid = applyUserid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }
}