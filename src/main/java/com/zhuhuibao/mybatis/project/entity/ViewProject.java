package com.zhuhuibao.mybatis.project.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 我查看过的项目信息
 * @author  penglong
 * @create 2016-05-27
 */
@ApiModel(value="我查看过的项目信息",description = "我查看过的项目信息")
public class ViewProject {
    @ApiModelProperty(value = "自增主键")
    private Long id;
    @ApiModelProperty(value = "查看项目信息人的ID")
    private Long viewerId;
    @ApiModelProperty(value = "查看项目信息人的企业ID")
    private Long companyId;
    @ApiModelProperty(value = "项目ID")
    private Long prjId;
    @ApiModelProperty(value = "查看时间")
    private Date viewTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getViewerId() {
        return viewerId;
    }

    public void setViewerId(Long viewerId) {
        this.viewerId = viewerId;
    }

    public Long getPrjId() {
        return prjId;
    }

    public void setPrjId(Long prjId) {
        this.prjId = prjId;
    }

    public Date getViewTime() {
        return viewTime;
    }

    public void setViewTime(Date viewTime) {
        this.viewTime = viewTime;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}