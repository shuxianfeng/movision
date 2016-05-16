package com.zhuhuibao.mybatis.oms.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.zhuhuibao.utils.pagination.util.StringUtils;

import java.util.Date;
/**
 * 招中标公告实体类
 * @author  penglong
 * @create 2016-05-13
 */
@ApiModel(value = "招中标公告类",description = "招中标公告类")
public class TenderToned {
    @ApiModelProperty(value = "招中标ID，主键")
    private Long id;
    @ApiModelProperty(value = "公告名称")
    private String noticeName;
    @ApiModelProperty(value="创建者ID")
    private Long createid;
    @ApiModelProperty(value = "发布日期")
    private Date publishDate;
    @ApiModelProperty(value = "修改日期")
    private Date updateDate;
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    @ApiModelProperty(value = "省代码")
    private String province;
    @ApiModelProperty(value = "市代码")
    private String city;
    @ApiModelProperty(value = "区代码")
    private String area;
    @ApiModelProperty(value = "公告类别1:招标公告，2：中标公告")
    private Integer type;
    @ApiModelProperty(value = "公告日期")
    private Date startDate;
    @ApiModelProperty(value = "截止日期")
    private Date endDate;
    @ApiModelProperty(value = "公告内容")
    private String content;
    @ApiModelProperty(value = "删除标识：1删除,0:不删除")
    private Byte is_deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNoticeName() {
        return noticeName;
    }

    public void setNoticeName(String noticeName) {
        this.noticeName = noticeName == null ? null : noticeName.trim();
    }

    public Long getCreateid() {
        return createid;
    }

    public void setCreateid(Long createid) {
        this.createid = createid;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Byte getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(Byte is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String toString()
    {
        return StringUtils.beanToString(this);
    }
}