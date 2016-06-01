package com.zhuhuibao.mybatis.tech.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.util.Date;
/**
 * 技术培训，专家培训申请的课程
 * @author  penglong
 * @create 2016-05-27
 */
@ApiModel(value = "TechExpertCourse",description = "技术培训，专家培训申请的课程")
public class TechExpertCourse {
    @ApiModelProperty(value = "申请课程ID，主键")
    private Long id;
    @ApiModelProperty(value = "申请人ID")
    private Long proposerId;
    @ApiModelProperty(value = "发布时间")
    private Date publishTime;
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    @ApiModelProperty(value = "课程名称")
    private String title;
    @ApiModelProperty(value = "主讲人")
    private String teacher;
    @ApiModelProperty(value = "城市代码")
    private String city;
    @ApiModelProperty(value = "联系人名称")
    private String linkman;
    @ApiModelProperty(value = "开课开始时间")
    private Date startTime;
    @ApiModelProperty(value = "开课结束时间")
    private Date endTime;
    @ApiModelProperty(value = "手机")
    private String mobile;
    @ApiModelProperty(value = "课程简介")
    private Integer status;
    @ApiModelProperty(value = "状态：1待处理，2已处理，3删除")
    private String notes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProposerId() {
        return proposerId;
    }

    public void setProposerId(Long proposerId) {
        this.proposerId = proposerId;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher == null ? null : teacher.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman == null ? null : linkman.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes == null ? null : notes.trim();
    }
}