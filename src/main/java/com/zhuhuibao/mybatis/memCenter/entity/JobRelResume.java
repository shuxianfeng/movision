package com.zhuhuibao.mybatis.memCenter.entity;

public class JobRelResume {
    private Long id;

    private Long jobID;

    private Long resumeID;

    private Long applicantId;

    private String publishDate;

    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobID() {
        return jobID;
    }

    public void setJobID(Long jobID) {
        this.jobID = jobID;
    }

    public Long getResumeID() {
        return resumeID;
    }

    public void setResumeID(Long resumeID) {
        this.resumeID = resumeID;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public Long getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(Long applicantId) {
        this.applicantId = applicantId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}