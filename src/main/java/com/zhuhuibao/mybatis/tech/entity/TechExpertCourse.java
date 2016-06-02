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
    @ApiModelProperty(value = "操作人ID")
    private Long operateId;
    @ApiModelProperty(value = "发布时间")
    private String publishTime;
    @ApiModelProperty(value = "修改时间")
    private String updateTime;
    @ApiModelProperty(value = "课程名称")
    private String title;
    @ApiModelProperty(value = "主讲人")
    private String teacher;
    @ApiModelProperty(value = "城市代码")
    private String city;
    @ApiModelProperty(value = "联系人名称")
    private String linkman;
    @ApiModelProperty(value = "开课开始时间")
    private String startTime;
    @ApiModelProperty(value = "开课结束时间")
    private String endTime;
    @ApiModelProperty(value = "手机")
    private String mobile;
    @ApiModelProperty(value = "状态：1待处理，2已处理，3删除")
    private Integer status;
    @ApiModelProperty(value = "课程简介")
    private String notes;
    @ApiModelProperty(value = "课程类型：1：技术培训，2专家培训")
    private Integer type;

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

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
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

    public Long getOperateId() {
        return operateId;
    }

    public void setOperateId(Long operateId) {
        this.operateId = operateId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}