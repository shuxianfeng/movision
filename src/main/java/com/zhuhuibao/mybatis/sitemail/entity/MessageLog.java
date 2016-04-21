package com.zhuhuibao.mybatis.sitemail.entity;

public class MessageLog {
    private Long id;

    private Long recID;

    private Long messageID;

    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecID() {
        return recID;
    }

    public void setRecID(Long recID) {
        this.recID = recID;
    }

    public Long getMessageID() {
        return messageID;
    }

    public void setMessageID(Long messageID) {
        this.messageID = messageID;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}