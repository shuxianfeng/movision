package com.zhuhuibao.mybatis.memCenter.entity;

import java.util.Date;

public class CertificateRecord {
    private Long id;

    private Integer mem_id;

    private String certificate_number;

    private String certificate_name;

    private String certificate_id;

    private Integer certificate_grade;

    private String certificate_url;

    private String type;

    private Date time;

    private Integer is_deleted;

    private String status;

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

    public String getCertificate_number() {
        return certificate_number;
    }

    public void setCertificate_number(String certificate_number) {
        this.certificate_number = certificate_number == null ? null : certificate_number.trim();
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

    public String getCertificate_id() {
        return certificate_id;
    }

    public void setCertificate_id(String certificate_id) {
        this.certificate_id = certificate_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}