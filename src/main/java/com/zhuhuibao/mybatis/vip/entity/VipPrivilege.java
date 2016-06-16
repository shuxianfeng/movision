package com.zhuhuibao.mybatis.vip.entity;

import java.io.Serializable;
import java.util.Date;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.zhuhuibao.common.constant.VipConstant.VipPrivilegeType;

/**
 * 
 * @author tongxinglong
 *
 */
public class VipPrivilege implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 718470400314161554L;

	/**
	 * 主键
	 */
	private Long id;

	@ApiModelProperty(value = "VIP级别，0：个人普通，30：个人黄金，60：个人铂金；100：企业普通，130：企业黄金，160：企业铂金", required = true)
	private int vipLevel;

	@ApiModelProperty(value = "特权类型,1:是否具有相应特权；2：折扣率；3:数量", required = true)
	private VipPrivilegeType type;
	
	@ApiModelProperty(value = "特权拼音", required = true)
	private String pinyin;

	@ApiModelProperty(value = "特权名称", required = true)
	private String name;

	@ApiModelProperty(value = "特权值", required = true)
	private Long value;

	@ApiModelProperty(value = "特权描述")
	private String description;
	/**
	 * 添加时间
	 */
	private Date addTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public VipPrivilegeType getType() {
		return type;
	}

	public void setType(VipPrivilegeType type) {
		this.type = type;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
