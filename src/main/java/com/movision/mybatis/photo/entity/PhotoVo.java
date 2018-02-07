package com.movision.mybatis.photo.entity;

import com.movision.mybatis.photoOrder.entity.PhotoOrder;
import com.movision.mybatis.photoOrder.entity.PhotoOrderVo;

import java.util.Date;
import java.util.List;

/**
 * @Author zhanglei
 * @Date 2018/2/1 11:50
 */
public class PhotoVo {
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

    private String nickname;

    private String photo;

    private List<PhotoOrderVo> photoOrders;

    public List<PhotoOrderVo> getPhotoOrders() {
        return photoOrders;
    }

    public void setPhotoOrders(List<PhotoOrderVo> photoOrders) {
        this.photoOrders = photoOrders;
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
        this.title = title;
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
        this.city = city;
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
        this.content = content;
    }

    public String getSubjectmatter() {
        return subjectmatter;
    }

    public void setSubjectmatter(String subjectmatter) {
        this.subjectmatter = subjectmatter;
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

    public Date getPubdate() {
        return pubdate;
    }

    public void setPubdate(Date pubdate) {
        this.pubdate = pubdate;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
