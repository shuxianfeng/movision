package com.movision.mybatis.afterservicestream.entity;

import java.util.Date;

public class AfterserviceStream {
    private Integer id;

    private Integer orderid;

    private Integer isdel;

    private String processingpeople;

    private Date processingtime;

    private Integer aftersalestatus;

    private Integer processingstatus;

    private String remark;

    private Integer afterserviceid;

    public Integer getAfterserviceid() {
        return afterserviceid;
    }

    public void setAfterserviceid(Integer afterserviceid) {
        this.afterserviceid = afterserviceid;
    }

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

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}