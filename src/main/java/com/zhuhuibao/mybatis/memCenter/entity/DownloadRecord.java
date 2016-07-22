package com.zhuhuibao.mybatis.memCenter.entity;

import java.util.Date;

public class DownloadRecord {
    private Long id;

    private Long resumeID;

    private Long companyID;

    private Long createId;

    private Date publishDate;

    private Integer is_deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResumeID() {
        return resumeID;
    }

    public void setResumeID(Long resumeID) {
        this.resumeID = resumeID;
    }

    public Long getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Long companyID) {
        this.companyID = companyID;
    }

    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Integer getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(Integer is_deleted) {
        this.is_deleted = is_deleted;
    }
}