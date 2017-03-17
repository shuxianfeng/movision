package com.movision.mybatis.logisticsCompany.entity;

public class LogisticsCompany {
    private Integer id;

    private String logisticscompany;

    private String code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogisticscompany() {
        return logisticscompany;
    }

    public void setLogisticscompany(String logisticscompany) {
        this.logisticscompany = logisticscompany == null ? null : logisticscompany.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }
}