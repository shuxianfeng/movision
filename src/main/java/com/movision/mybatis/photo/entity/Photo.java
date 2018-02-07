package com.movision.mybatis.photo.entity;

import java.util.Date;

public class Photo {
    private Integer id;

    private Integer isdel;

    private String title;

    private Date intime;

    private Double money;

    private String city;

    private Integer returnmap;

    private Integer personnal;

    private String content;

    private String subjectmatter;

    private Integer personnalnumber;

    private Integer userid;

    private Date pubdate;

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getPubdate() {
        return pubdate;
    }

    public void setPubdate(Date pubdate) {
        this.pubdate = pubdate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public Integer getReturnmap() {
        return returnmap;
    }

    public void setReturnmap(Integer returnmap) {
        this.returnmap = returnmap;
    }

    public Integer getPersonnal() {
        return personnal;
    }

    public void setPersonnal(Integer personnal) {
        this.personnal = personnal;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getSubjectmatter() {
        return subjectmatter;
    }

    public void setSubjectmatter(String subjectmatter) {
        this.subjectmatter = subjectmatter == null ? null : subjectmatter.trim();
    }

    public Integer getPersonnalnumber() {
        return personnalnumber;
    }

    public void setPersonnalnumber(Integer personnalnumber) {
        this.personnalnumber = personnalnumber;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}