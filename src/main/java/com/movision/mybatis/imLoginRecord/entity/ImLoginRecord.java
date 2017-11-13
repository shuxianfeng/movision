package com.movision.mybatis.imLoginRecord.entity;

import java.util.Date;

public class ImLoginRecord {
    private Integer id;

    private String eventType;

    private String accid;

    private String clientIp;

    private String clientType;

    private String code;

    private String sdkVersion;

    private Date timestamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid == null ? null : accid.trim();
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public String getEventType() {

        return eventType;
    }

    public String getClientIp() {
        return clientIp;
    }

    public String getClientType() {
        return clientType;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {

        return timestamp;
    }
}