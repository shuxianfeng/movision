package com.movision.mybatis.goodsAssessment.entity;

import com.movision.mybatis.goodsAssessmentImg.entity.GoodsAssessmentImg;

import java.util.Date;
import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/2/16 10:11
 */
public class GoodsAssessmentVo {
    private Integer id;

    private Integer userid;

    private Integer goodid;

    private Integer suborderid;

    private Integer pid;

    private Integer goodpoint;

    private Integer logisticpoint;

    private Integer servicepoint;

    private String content;

    private Integer isimage;

    private Integer isanonymity;

    private Date mintime;
    private Date maxtime;

    public Date getMintime() {
        return mintime;
    }

    public void setMintime(Date mintime) {
        this.mintime = mintime;
    }

    public Date getMaxtime() {
        return maxtime;
    }

    public void setMaxtime(Date maxtime) {
        this.maxtime = maxtime;
    }

    private Date createtime;

    private String photo;//评论用户的头像图片url

    private String nickname;//评价用户的昵称

    private String phone;//评价用户的手机号

    private GoodsAssessmentVo goodsAssessmentVo;//用户的父评论（用于官方回复用户的评论）

    private List<GoodsAssessmentImg> goodsAssessmentImgList;//评论的晒单图片列表

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

    public Integer getGoodid() {
        return goodid;
    }

    public void setGoodid(Integer goodid) {
        this.goodid = goodid;
    }

    public Integer getSuborderid() {
        return suborderid;
    }

    public void setSuborderid(Integer suborderid) {
        this.suborderid = suborderid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getGoodpoint() {
        return goodpoint;
    }

    public void setGoodpoint(Integer goodpoint) {
        this.goodpoint = goodpoint;
    }

    public Integer getLogisticpoint() {
        return logisticpoint;
    }

    public void setLogisticpoint(Integer logisticpoint) {
        this.logisticpoint = logisticpoint;
    }

    public Integer getServicepoint() {
        return servicepoint;
    }

    public void setServicepoint(Integer servicepoint) {
        this.servicepoint = servicepoint;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIsimage() {
        return isimage;
    }

    public void setIsimage(Integer isimage) {
        this.isimage = isimage;
    }

    public Integer getIsanonymity() {
        return isanonymity;
    }

    public void setIsanonymity(Integer isanonymity) {
        this.isanonymity = isanonymity;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public GoodsAssessmentVo getGoodsAssessmentVo() {
        return goodsAssessmentVo;
    }

    public void setGoodsAssessmentVo(GoodsAssessmentVo goodsAssessmentVo) {
        this.goodsAssessmentVo = goodsAssessmentVo;
    }

    public List<GoodsAssessmentImg> getGoodsAssessmentImgList() {
        return goodsAssessmentImgList;
    }

    public void setGoodsAssessmentImgList(List<GoodsAssessmentImg> goodsAssessmentImgList) {
        this.goodsAssessmentImgList = goodsAssessmentImgList;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
