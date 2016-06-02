package com.zhuhuibao.mybatis.tech.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 技术、专家培训课程
 * @author  penglong
 * @create 2016-05-27
 */
@ApiModel(value = "TrainPublishCourse",description = "技术、专家培训课程")
public class TrainPublishCourse {
    @ApiModelProperty(value="课程ID")
    private Long courseid;
    @ApiModelProperty(value="发布者ID")
    private Long publisherid;
    @ApiModelProperty(value="发布时间")
    private String publishTime;
    @ApiModelProperty(value="购买开始时间(上架时间)")
    private String saleTime;
    @ApiModelProperty(value="修改时间")
    private String updateTime;
    @ApiModelProperty(value="培训标题")
    private String title;
    @ApiModelProperty(value="主讲人")
    private String teacher;
    @ApiModelProperty(value="培训开始时间")
    private String startTime;
    @ApiModelProperty(value="培训结束时间")
    private String endTime;
    @ApiModelProperty(value="省代码")
    private String province;
    @ApiModelProperty(value="区代码")
    private String area;
    @ApiModelProperty(value="详细地址")
    private String address;
    @ApiModelProperty(value="城市代码")
    private String city;
    @ApiModelProperty(value="培训价格  元/人")
    private BigDecimal price;
    @ApiModelProperty(value="库存数量（最大购买数）")
    private Integer storageNumber;
    @ApiModelProperty(value="最少购买数量,最低开课条件")
    private String minBuyNumber;
    @ApiModelProperty(value="最大购买数量")
    private String maxBuyNumber;
    @ApiModelProperty(value="购买截止日期")
    private String expiryDate;
    @ApiModelProperty(value="培训轮播图，多个用分号隔开")
    private String imgUrl;
    @ApiModelProperty(value="培训注意事项")
    private String notice;
    @ApiModelProperty(value="课程类型：1技术培训，2专家培训")
    private Integer courseType;
    @ApiModelProperty(value="状态：1未上架，2销售中，3待开课，4已终止，5已完成")
    private Integer status;
    @ApiModelProperty(value="课程简介")
    private String notes;

    public Long getCourseid() {
        return courseid;
    }

    public void setCourseid(Long courseid) {
        this.courseid = courseid;
    }

    public Long getPublisherid() {
        return publisherid;
    }

    public void setPublisherid(Long publisherid) {
        this.publisherid = publisherid;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(String saleTime) {
        this.saleTime = saleTime;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStorageNumber() {
        return storageNumber;
    }

    public void setStorageNumber(Integer storageNumber) {
        this.storageNumber = storageNumber;
    }

    public String getMinBuyNumber() {
        return minBuyNumber;
    }

    public void setMinBuyNumber(String minBuyNumber) {
        this.minBuyNumber = minBuyNumber == null ? null : minBuyNumber.trim();
    }

    public String getMaxBuyNumber() {
        return maxBuyNumber;
    }

    public void setMaxBuyNumber(String maxBuyNumber) {
        this.maxBuyNumber = maxBuyNumber == null ? null : maxBuyNumber.trim();
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice == null ? null : notice.trim();
    }

    public Integer getCourseType() {
        return courseType;
    }

    public void setCourseType(Integer courseType) {
        this.courseType = courseType;
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