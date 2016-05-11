package com.zhuhuibao.mybatis.oms.entity;

import java.util.Date;
import java.util.List;

/**
 * 项目工程信息
 * @author 李光明
 * @since 2016-05-10
 *
 */
public class ProjectInfo {
	private Long id; // 主键
	private String name; // 项目名称
	private Long createid; // 创建者ID
	private Date publishDate; // 发布日期
	private Date updateDate; // 修改日期
	private String province; // 省代码
	private String city; // 市代码
	private String area; // 区代码
	private String address; // 地址
	private Integer category; // 项目类别 type=8
	private Double price; // 工程造价
	private Date startDate; // 开工日期
	private Date endDate; // 竣工日期
	private String description; // 项目描述
	private String record; // 跟进记录
	private String is_deleted; // 删除标识：1删除,0:不删除
	private List partyAList;//甲方信息	
	private List partyBList;//甲方信息	
	

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
		this.name = name;
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

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRecord() {
		return record;
	}

	public void setRecord(String record) {
		this.record = record;
	}

	public String getIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(String is_deleted) {
		this.is_deleted = is_deleted;
	}

	public List getPartyAList() {
		return partyAList;
	}

	public void setPartyAList(List partyAList) {
		this.partyAList = partyAList;
	}

	public List getPartyBList() {
		return partyBList;
	}

	public void setPartyBList(List partyBList) {
		this.partyBList = partyBList;
	}

}
