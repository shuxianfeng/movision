package com.movision.mybatis.logidticsRelation.entity;

public class LogidticsRelation {
    private Integer id;

    private String logisticsid;

    private Integer companyid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogisticsid() {
        return logisticsid;
    }

    public void setLogisticsid(String logisticsid) {
        this.logisticsid = logisticsid == null ? null : logisticsid.trim();
    }

    public Integer getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Integer companyid) {
        this.companyid = companyid;
    }
}