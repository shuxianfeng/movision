package com.zhuhuibao.mybatis.order.entity;

import java.math.BigDecimal;
import java.util.Date;

public class ZhbAccount {
    private Long id;

    private Long memberId;

    private String status;

    private BigDecimal eMoney;

    private Date addTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public BigDecimal geteMoney() {
        return eMoney;
    }

    public void seteMoney(BigDecimal eMoney) {
        this.eMoney = eMoney;
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