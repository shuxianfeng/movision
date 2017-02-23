package com.movision.mybatis.post.entity;

import java.util.Date;

/**
 * @Author zhurui
 * @Date 2017/2/21 12:59
 */
public class PostSpread {
    private String title;//帖子标题
    private String circleid;//圈子id
    private String userid;//用户id
    private String postcontent;//评论内容
    private String endtime;//结束时间
    private String begintime;//开始时间
    private String essencedate;//加精时间
    private String pai;//排序

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCircleid() {
        return circleid;
    }

    public void setCircleid(String circleid) {
        this.circleid = circleid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPostcontent() {
        return postcontent;
    }

    public void setPostcontent(String postcontent) {
        this.postcontent = postcontent;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getEssencedate() {
        return essencedate;
    }

    public void setEssencedate(String essencedate) {
        this.essencedate = essencedate;
    }

    public String getPai() {
        return pai;
    }

    public void setPai(String pai) {
        this.pai = pai;
    }
}
