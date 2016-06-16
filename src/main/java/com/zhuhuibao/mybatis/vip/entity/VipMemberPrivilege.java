package com.zhuhuibao.mybatis.vip.entity;

import java.io.Serializable;
import java.util.Date;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.zhuhuibao.common.constant.VipConstant.VipPrivilegeType;

/**
 * 会员自定义特权信息
 * 
 * @author tongxinglong
 *
 */
public class VipMemberPrivilege implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5987154777438609541L;
	/**
	 * 主键
	 */
	private Long id;

	@ApiModelProperty(value = "会员ID", required = true)
	private Long memberId;

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

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
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
