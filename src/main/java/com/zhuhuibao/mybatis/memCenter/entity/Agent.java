package com.zhuhuibao.mybatis.memCenter.entity;

public class Agent {
    private Long id;

    private String brandid;

    private String scateid;

    private String agentid;

    private String proviceid;

    private String createid;

    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandid() {
        return brandid;
    }

    public void setBrandid(String brandid) {
        this.brandid = brandid;
    }

    public String getScateid() {
        return scateid;
    }

    public void setScateid(String scateid) {
        this.scateid = scateid == null ? null : scateid.trim();
    }

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public String getProviceid() {
        return proviceid;
    }

    public void setProviceid(String proviceid) {
        this.proviceid = proviceid == null ? null : proviceid.trim();
    }

    public String getCreateid() {
        return createid;
    }

    public void setCreateid(String createid) {
        this.createid = createid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}