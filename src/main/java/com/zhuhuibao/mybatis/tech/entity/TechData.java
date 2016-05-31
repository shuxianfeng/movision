package com.zhuhuibao.mybatis.tech.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 上传的技术资料
 * @author  penglong
 * @create 2016-05-27
 */
@ApiModel(value = "上传的技术资料",description = "上传的技术资料")
public class TechData {
    @ApiModelProperty(value = "技术资料ID，主键")
    private Long id;
    @ApiModelProperty(value = "创建者ID")
    private Long createid;
    @ApiModelProperty(value = "发布时间")
    private String publishTime;
    @ApiModelProperty(value = "修改时间")
    private String updateTime;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "一级分类包含：行业解决方案，技术资料，培训资料")
    private Integer fCategory;
    @ApiModelProperty(value = "二级分类")
    private Integer sCategory;
    @ApiModelProperty(value = "标签，用于关键字查询")
    private String tag;
    @ApiModelProperty(value = "1:普通资料，2：付费资料")
    private Integer type;
    @ApiModelProperty(value = "状态：1：待审核，2：已审核，3：拒绝，4：删除")
    private Integer status;
    @ApiModelProperty(value = "点击率")
    private Long views;
    @ApiModelProperty(value = "下载数")
    private Long download;
    @ApiModelProperty(value = "附件")
    private String attach;
    @ApiModelProperty(value = "简介")
    private String notes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateid() {
        return createid;
    }

    public void setCreateid(Long createid) {
        this.createid = createid;
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

    public Integer getfCategory() {
        return fCategory;
    }

    public void setfCategory(Integer fCategory) {
        this.fCategory = fCategory;
    }

    public Integer getsCategory() {
        return sCategory;
    }

    public void setsCategory(Integer sCategory) {
        this.sCategory = sCategory;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag == null ? null : tag.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Long getDownload() {
        return download;
    }

    public void setDownload(Long download) {
        this.download = download;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach == null ? null : attach.trim();
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes == null ? null : notes.trim();
    }
}