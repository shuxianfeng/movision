package com.movision.mybatis.afterservice.entity;

import java.util.Date;

public class Afterservice {
    private Integer id;

    private Integer orderid;

    private Integer addressid;

    private Integer goodsid;

    private Integer afterstatue;

    private Integer aftersalestatus;

    private Integer processingstatus;

    private Integer isdel;

    private Double refundamount;

    private String processingpeople;

    private Date processingtime;

    private Integer userid;

    private Date proposertime;

    private Double amountdue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public Integer getAddressid() {
        return addressid;
    }

    public void setAddressid(Integer addressid) {
        this.addressid = addressid;
    }

    public Integer getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    public Integer getAfterstatue() {
        return afterstatue;
    }

    public void setAfterstatue(Integer afterstatue) {
        this.afterstatue = afterstatue;
    }

    public Integer getAftersalestatus() {
        return aftersalestatus;
    }

    public void setAftersalestatus(Integer aftersalestatus) {
        this.aftersalestatus = aftersalestatus;
    }

    public Integer getProcessingstatus() {
        return processingstatus;
    }

    public void setProcessingstatus(Integer processingstatus) {
        this.processingstatus = processingstatus;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public Double getRefundamount() {
        return refundamount;
    }

    public void setRefundamount(Double refundamount) {
        this.refundamount = refundamount;
    }

    public String getProcessingpeople() {
        return processingpeople;
    }

    public void setProcessingpeople(String processingpeople) {
        this.processingpeople = processingpeople == null ? null : processingpeople.trim();
    }

    public Date getProcessingtime() {
        return processingtime;
    }

    public void setProcessingtime(Date processingtime) {
        this.processingtime = processingtime;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Date getProposertime() {
        return proposertime;
    }

    public void setProposertime(Date proposertime) {
        this.proposertime = proposertime;
    }

    public Double getAmountdue() {
        return amountdue;
    }

    public void setAmountdue(Double amountdue) {
        this.amountdue = amountdue;
    }
}