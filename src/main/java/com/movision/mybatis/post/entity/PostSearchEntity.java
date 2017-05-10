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

    private Integer type;

    private Date intime;

    private Double activefee;

    private Date begintime;

    private Date endtime;

    private String imgurl;

    private Integer activetype;

    private String coverimg;

    private Integer enddays;//距离活动结束剩余天数（查询活动时该字段不为空）

    private Integer partsum;//已参与活动总人数

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
                ", type=" + type +
                ", intime=" + intime +
                ", activefee=" + activefee +
                ", begintime=" + begintime +
                ", endtime=" + endtime +
                ", imgurl='" + imgurl + '\'' +
                ", activetype=" + activetype +
                ", coverimg='" + coverimg + '\'' +
                ", enddays=" + enddays +
                ", partsum=" + partsum +
                '}';
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

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
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

    public String getImgurl() {
        return imgurl;
    }
}
