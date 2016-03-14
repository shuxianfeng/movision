package com.zhuhuibao.mybatis.product.entity;

import java.util.Date;

public class Product {
    private Long id;

    private String name;

    private Long createid;

    private Integer fcateid;

    private Integer scateid;
    
    private String categoryName;

    private Integer brandid;

    private Integer status;

    private String publishTime;

    private Date lastmodified;

    private Double price;

    private String unit;

    private Double repository;

    private Double number;

    private Integer hit;

    private String imgUrl;

    private String paramIDs;

    private String paramValues;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getCreateid() {
        return createid;
    }

    public void setCreateid(Long createid) {
        this.createid = createid;
    }

    public Integer getFcateid() {
        return fcateid;
    }

    public void setFcateid(Integer fcateid) {
        this.fcateid = fcateid;
    }

    public Integer getScateid() {
        return scateid;
    }

    public void setScateid(Integer scateid) {
        this.scateid = scateid;
    }

    public Integer getBrandid() {
        return brandid;
    }

    public void setBrandid(Integer brandid) {
        this.brandid = brandid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public Date getLastmodified() {
        return lastmodified;
    }

    public void setLastmodified(Date lastmodified) {
        this.lastmodified = lastmodified;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public Double getRepository() {
        return repository;
    }

    public void setRepository(Double repository) {
        this.repository = repository;
    }

    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    public Integer getHit() {
        return hit;
    }

    public void setHit(Integer hit) {
        this.hit = hit;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    public String getParamIDs() {
        return paramIDs;
    }

    public void setParamIDs(String paramIDs) {
        this.paramIDs = paramIDs == null ? null : paramIDs.trim();
    }

    public String getParamValues() {
        return paramValues;
    }

    public void setParamValues(String paramValues) {
        this.paramValues = paramValues == null ? null : paramValues.trim();
    }

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
    
}