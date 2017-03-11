package com.movision.mybatis.goodsAssessment.entity;

/**
 * @Author shuxf
 * @Date 2017/2/16 15:26
 */
public class GoodsAssessmentCategery {

    private String allname;//全部评论名称

    private Integer allsum;//全部评论数

    private String imgname;//有图评论名称

    private Integer imgsum;//有图的评论数

    private String qualityname;//质量好的名称

    private Integer qualitysum;//质量好的评论数

    private String fastname;//送货快的名称

    private Integer fastsum;//送货快的评论数

    private String attitudename;//态度好的名称

    private Integer attitudesum;//态度好的评论数

    private String qualitygeneralname;//质量一般的名称

    private Integer qualitygeneralsum;//质量一般的评论数

    public Integer getAllsum() {
        return allsum;
    }

    public void setAllsum(Integer allsum) {
        this.allsum = allsum;
    }

    public Integer getImgsum() {
        return imgsum;
    }

    public void setImgsum(Integer imgsum) {
        this.imgsum = imgsum;
    }

    public Integer getQualitysum() {
        return qualitysum;
    }

    public void setQualitysum(Integer qualitysum) {
        this.qualitysum = qualitysum;
    }

    public Integer getFastsum() {
        return fastsum;
    }

    public void setFastsum(Integer fastsum) {
        this.fastsum = fastsum;
    }

    public Integer getAttitudesum() {
        return attitudesum;
    }

    public void setAttitudesum(Integer attitudesum) {
        this.attitudesum = attitudesum;
    }

    public Integer getQualitygeneralsum() {
        return qualitygeneralsum;
    }

    public void setQualitygeneralsum(Integer qualitygeneralsum) {
        this.qualitygeneralsum = qualitygeneralsum;
    }

    public String getAllname() {
        return allname;
    }

    public void setAllname(String allname) {
        this.allname = allname;
    }

    public String getImgname() {
        return imgname;
    }

    public void setImgname(String imgname) {
        this.imgname = imgname;
    }

    public String getQualityname() {
        return qualityname;
    }

    public void setQualityname(String qualityname) {
        this.qualityname = qualityname;
    }

    public String getFastname() {
        return fastname;
    }

    public void setFastname(String fastname) {
        this.fastname = fastname;
    }

    public String getAttitudename() {
        return attitudename;
    }

    public void setAttitudename(String attitudename) {
        this.attitudename = attitudename;
    }

    public String getQualitygeneralname() {
        return qualitygeneralname;
    }

    public void setQualitygeneralname(String qualitygeneralname) {
        this.qualitygeneralname = qualitygeneralname;
    }
}
