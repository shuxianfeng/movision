package com.zhuhuibao.mybatis.sitemail.entity;

import java.io.Serializable;
import java.util.Date;

public class MessageText implements Serializable{

    private static final long serialVersionUID = 7047354171644129664L;

    private Long id;

    private Long sendID;

    private Long recID;

    private String title;

    private String messageText;

    private Integer type;

    private Date sendTime;

    private Long typeID;
    
    private String source;
    

    public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSendID() {
        return sendID;
    }

    public void setSendID(Long sendID) {
        this.sendID = sendID;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText == null ? null : messageText.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Long getTypeID() {
        return typeID;
    }

    public void setTypeID(Long typeID) {
        this.typeID = typeID;
    }

    public Long getRecID() {
        return recID;
    }

    public void setRecID(Long recID) {
        this.recID = recID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}