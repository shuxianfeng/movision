package com.zhuhuibao.mybatis.advertising.entity;

public class SysAdvAttr {
    private Integer id;

    private String chanType;

    private String page;

    private String pageDesc;

    private String advArea;

    private String advAreaDesc;

    private String advType;

    private String advTypeDesc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChanType() {
        return chanType;
    }

    public void setChanType(String chanType) {
        this.chanType = chanType == null ? null : chanType.trim();
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page == null ? null : page.trim();
    }

    public String getPageDesc() {
        return pageDesc;
    }

    public void setPageDesc(String pageDesc) {
        this.pageDesc = pageDesc == null ? null : pageDesc.trim();
    }

    public String getAdvArea() {
        return advArea;
    }

    public void setAdvArea(String advArea) {
        this.advArea = advArea == null ? null : advArea.trim();
    }

    public String getAdvAreaDesc() {
        return advAreaDesc;
    }

    public void setAdvAreaDesc(String advAreaDesc) {
        this.advAreaDesc = advAreaDesc == null ? null : advAreaDesc.trim();
    }

    public String getAdvType() {
        return advType;
    }

    public void setAdvType(String advType) {
        this.advType = advType == null ? null : advType.trim();
    }

    public String getAdvTypeDesc() {
        return advTypeDesc;
    }

    public void setAdvTypeDesc(String advTypeDesc) {
        this.advTypeDesc = advTypeDesc == null ? null : advTypeDesc.trim();
    }
}