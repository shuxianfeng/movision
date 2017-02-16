package com.movision.mybatis.goodsAssessment.entity;

/**
 * @Author shuxf
 * @Date 2017/2/16 15:26
 */
public class GoodsAssessmentCategery {

    private Integer allsum;//全部评论数

    private Integer imgsum;//有图的评论数

    private Integer qualitysum;//质量好的评论数

    private Integer fastsum;//送货快的评论数

    private Integer attitudesum;//态度好的评论数

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
}
