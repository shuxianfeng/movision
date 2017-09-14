package com.movision.mybatis.post.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/27 16:41
 */
public class PostSearchEntity implements Serializable {
    private Integer id;

    private Integer circleid;

    private String circlename;

    private String title;

    private String subtitle;

    private String postcontent;

    private Integer isactive;

    private Integer partsum_enddays;//当前活动显示参与人数还是显示剩余结束天数 0 显示结束天数 1 显示活动参与人数

    private Integer type;

    private Date intime;

    private Double activefee;

    private Date begintime;

    private Date endtime;

    private Integer activetype;

    private String coverimg;

    private Integer enddays;//距离活动结束剩余天数（查询活动时该字段不为空）

    private Integer partsum;//已参与活动总人数

    private Integer userid;

    private String nickname;

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getUserid() {

        return userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setEnddays(Integer enddays) {
        this.enddays = enddays;
    }

    public void setPartsum(Integer partsum) {
        this.partsum = partsum;
    }

    public Integer getEnddays() {

        return enddays;
    }

    public Integer getPartsum() {
        return partsum;
    }



    public void setCoverimg(String coverimg) {
        this.coverimg = coverimg;
    }

    public String getCoverimg() {

        return coverimg;
    }

    public void setActivetype(Integer activetype) {
        this.activetype = activetype;
    }

    public Integer getActivetype() {

        return activetype;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCircleid(Integer circleid) {
        this.circleid = circleid;
    }

    public void setCirclename(String circlename) {
        this.circlename = circlename;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setPostcontent(String postcontent) {
        this.postcontent = postcontent;
    }

    public void setIsactive(Integer isactive) {
        this.isactive = isactive;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public void setActivefee(Double activefee) {
        this.activefee = activefee;
    }

    public void setBegintime(Date begintime) {
        this.begintime = begintime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }


    public Integer getId() {

        return id;
    }

    public Integer getCircleid() {
        return circleid;
    }

    public String getCirclename() {
        return circlename;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getPostcontent() {
        return postcontent;
    }

    public Integer getIsactive() {
        return isactive;
    }

    public Integer getType() {
        return type;
    }

    public Date getIntime() {
        return intime;
    }

    public Double getActivefee() {
        return activefee;
    }

    public Date getBegintime() {
        return begintime;
    }

    public Date getEndtime() {
        return endtime;
    }


    public Integer getPartsum_enddays() {
        return partsum_enddays;
    }

    public void setPartsum_enddays(Integer partsum_enddays) {
        this.partsum_enddays = partsum_enddays;
    }

    @Override
    public String toString() {
        return "PostSearchEntity{" +
                "id=" + id +
                ", circleid=" + circleid +
                ", circlename='" + circlename + '\'' +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", postcontent='" + postcontent + '\'' +
                ", isactive=" + isactive +
                ", partsum_enddays=" + partsum_enddays +
                ", type=" + type +
                ", intime=" + intime +
                ", activefee=" + activefee +
                ", begintime=" + begintime +
                ", endtime=" + endtime +
                ", activetype=" + activetype +
                ", coverimg='" + coverimg + '\'' +
                ", enddays=" + enddays +
                ", partsum=" + partsum +
                ", userid=" + userid +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
