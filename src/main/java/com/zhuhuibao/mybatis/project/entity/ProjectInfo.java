package com.zhuhuibao.mybatis.project.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.zhuhuibao.utils.pagination.util.StringUtils;

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
	private String publishDate;
	@ApiModelProperty(value = "修改日期")
	private String updateDate;
	@ApiModelProperty(value = "省代码")
	private String province;
	@ApiModelProperty(value = "市代码")
	private String city;
	@ApiModelProperty(value = "区代码")
	private String area;
	@ApiModelProperty(value = "地址")
	private String address;
	@ApiModelProperty(value = " 项目类别 type=8")
	private String category;
	@ApiModelProperty(value = "项目类别名称")
	private String categoryName;
	@ApiModelProperty(value = " 工程造价")
	private Double price;
	@ApiModelProperty(value = " 开工日期")
	private String startDate;
	@ApiModelProperty(value = " 竣工日期")
	private String endDate;
	@ApiModelProperty(value = " 项目描述")
	private String description;
	@ApiModelProperty(value = " 项目概况")
	private String basicDesc;
	@ApiModelProperty(value = " 项目背景")
	private String background;
	@ApiModelProperty(value = " 备注")
	private String remark;
	@ApiModelProperty(value = " 跟进记录")
	private String record;
	@ApiModelProperty(value = " 删除标识：1删除,0:不删除")
	private String is_deleted;
	@ApiModelProperty(value = " 甲方信息集合")
	private List<ProjectLinkman> partyAList;
	@ApiModelProperty(value = " 乙方信息集合")
	private List<ProjectLinkman> partyBList;

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

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

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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

	public String getBasicDesc() {
		return basicDesc;
	}

	public void setBasicDesc(String basicDesc) {
		this.basicDesc = basicDesc;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return StringUtils.beanToString(this);
	}
}
