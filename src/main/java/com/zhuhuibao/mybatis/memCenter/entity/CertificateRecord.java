package com.zhuhuibao.mybatis.memCenter.entity;

import java.util.Date;

public class CertificateRecord {
    private Long id;

    private Integer mem_id;

    private String organization;

    private String certificate_name;

    private Integer certificate_grade;

    private String certificate_grade_name;

    private String certificate_url;

    private String type;

    private Date time;

    private Integer is_deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMem_id() {
        return mem_id;
    }

    public void setMem_id(Integer mem_id) {
        this.mem_id = mem_id;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization == null ? null : organization.trim();
    }

    public String getCertificate_name() {
        return certificate_name;
    }

    public void setCertificate_name(String certificate_name) {
        this.certificate_name = certificate_name == null ? null : certificate_name.trim();
    }

    public Integer getCertificate_grade() {
        return certificate_grade;
    }

    public void setCertificate_grade(Integer certificate_grade) {
        this.certificate_grade = certificate_grade;
    }

    public String getCertificate_grade_name() {
        return certificate_grade_name;
    }

    public void setCertificate_grade_name(String certificate_grade_name) {
        this.certificate_grade_name = certificate_grade_name == null ? null : certificate_grade_name.trim();
    }

    public String getCertificate_url() {
        return certificate_url;
    }

    public void setCertificate_url(String certificate_url) {
        this.certificate_url = certificate_url == null ? null : certificate_url.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(Integer is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}