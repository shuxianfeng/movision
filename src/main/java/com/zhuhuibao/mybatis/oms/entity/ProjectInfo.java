package com.zhuhuibao.mybatis.oms.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.zhuhuibao.utils.pagination.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 项目工程信息
 * @author 李光明
 * @since 2016-05-10
 *
 */
@ApiModel(value = "项目信息",description = "项目频道信息")
public class ProjectInfo {
	@ApiModelProperty(value = "项目ID，主键",required = true)
	private Long id;
	@ApiModelProperty(value = "项目名称")
	private String name;
	@ApiModelProperty(value = "创建者ID")
	private Long createid;
	@ApiModelProperty(value = "发布日期")
	private Date publishDate;
	@ApiModelProperty(value = "修改日期")
	private Date updateDate;
	@ApiModelProperty(value = "省代码")
	private String province;
	@ApiModelProperty(value = "市代码")
	private String city;
	@ApiModelProperty(value = "区代码")
	private String area;
	@ApiModelProperty(value = "地址")
	private String address;
	@ApiModelProperty(value = " 项目类别 type=8")
	private Integer category;
	@ApiModelProperty(value = " 工程造价")
	private Double price;
	@ApiModelProperty(value = " 公告日期")
	private Date startDate;
	@ApiModelProperty(value = " 截止日期")
	private Date endDate;
	@ApiModelProperty(value = " 项目描述")
	private String description;
	@ApiModelProperty(value = " 跟进记录")
	private String record;
	@ApiModelProperty(value = " 删除标识：1删除,0:不删除")
	private String is_deleted;
	@ApiModelProperty(value = " 甲方信息集合")
	private List<ProjectLinkman> partyAList;
	@ApiModelProperty(value = " 乙方信息集合")
	private List<ProjectLinkman> partyBList;
	

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

	@Override
	public String toString() {
		return StringUtils.beanToString(this);
	}
}
