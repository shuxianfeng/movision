package com.movision.mybatis.imFirstDialogue.entity;

import com.movision.mybatis.imuser.entity.ImUser;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author zhanglei
 * @Date 2017/4/13 11:30
 */
public class ImFirstDialogueVo implements Serializable {
    private Integer id;

    private Integer userid;

    private String fromid;

    private String toid;

    private String body;

    private Date createTime;
    private String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Integer isread;

    public Integer getIsread() {
        return isread;
    }

    public void setIsread(Integer isread) {
        this.isread = isread;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getFromid() {
        return fromid;
    }

    public void setFromid(String fromid) {
        this.fromid = fromid == null ? null : fromid.trim();
    }

    public String getToid() {
        return toid;
    }

    public void setToid(String toid) {
        this.toid = toid == null ? null : toid.trim();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body == null ? null : body.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
