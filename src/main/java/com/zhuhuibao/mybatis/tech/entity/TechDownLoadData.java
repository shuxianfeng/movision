package com.zhuhuibao.mybatis.tech.entity;

public class TechDownLoadData {
    private Long id;

    private Long createId;

    private Long dataId;

    private String dowloadTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public String getDowloadTime() {
        return dowloadTime;
    }

    public void setDowloadTime(String dowloadTime) {
        this.dowloadTime = dowloadTime;
    }
}