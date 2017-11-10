package com.movision.mybatis.imLoginRecord.entity;

public class ImLoginRecord {
    private Integer id;

    private String eventtype;

    private String accid;

    private String clientip;

    private String clienttype;

    private String code;

    private String sdkversion;

    private String timestamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEventtype() {
        return eventtype;
    }

    public void setEventtype(String eventtype) {
        this.eventtype = eventtype == null ? null : eventtype.trim();
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid == null ? null : accid.trim();
    }

    public String getClientip() {
        return clientip;
    }

    public void setClientip(String clientip) {
        this.clientip = clientip == null ? null : clientip.trim();
    }

    public String getClienttype() {
        return clienttype;
    }

    public void setClienttype(String clienttype) {
        this.clienttype = clienttype == null ? null : clienttype.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getSdkversion() {
        return sdkversion;
    }

    public void setSdkversion(String sdkversion) {
        this.sdkversion = sdkversion == null ? null : sdkversion.trim();
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp == null ? null : timestamp.trim();
    }
}