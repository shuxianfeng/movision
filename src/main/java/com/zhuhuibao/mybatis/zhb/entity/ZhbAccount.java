package com.zhuhuibao.mybatis.zhb.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.zhuhuibao.common.constant.ZhbConstant.ZhbAccountStatus;

/**
 * 筑慧币账户信息
 * 
 * @author tongxinglong
 *
 */
public class ZhbAccount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7914873313135030006L;

	/**
	 * 记录流水ID
	 */
	private Long id;

	@ApiModelProperty(value = "会员ID", required = true)
	private Long memberId;

	@ApiModelProperty(value = "账户状态，1：账户未冻结，2：账户已冻结", required = true)
	private ZhbAccountStatus status;

	@ApiModelProperty(value = "筑慧币金额", required = true)
	private BigDecimal amount;

	private Date addTime;

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

	public ZhbAccountStatus getStatus() {
		return status;
	}

	public void setStatus(ZhbAccountStatus status) {
		this.status = status;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * 获得账户余额的double值，保留两位小数
	 * 
	 * @return
	 */
	public double getAmountDoubleValue() {
		return null != amount ? amount.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() : 0;
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
