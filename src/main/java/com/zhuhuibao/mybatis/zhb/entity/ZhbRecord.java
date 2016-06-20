package com.zhuhuibao.mybatis.zhb.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.wordnik.swagger.annotations.ApiModelProperty;
import com.zhuhuibao.common.constant.ZhbConstant.ZhbRecordType;

/**
 * 筑慧币流水
 * 
 * @author tongxinglong
 *
 */
public class ZhbRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7288927263117774392L;

	/**
	 * 主键ID
	 */
	private Long id;

	@ApiModelProperty(value = "订单号", required = true)
	private String orderNo;

	@ApiModelProperty(value = "购买人ID", required = true)
	private Long buyerId;

	@ApiModelProperty(value = "操作人ID", required = true)
	private Long operaterId;

	@ApiModelProperty(value = "金额", required = true)
	private BigDecimal amount;
	/**
	 * 支付状态，1：成功，2：失败
	 */
	private String status;

	@ApiModelProperty(value = "记录类型，1：支付，2：充值，3：退款", required = true)
	private ZhbRecordType type;

	@ApiModelProperty(value = "购买的物品类型")
	private String goodsType;

	@ApiModelProperty(value = "购买的物品ID")
	private Long goodsId;

	@ApiModelProperty(value = "描述说明")
	private String description;
	private Date addTime;
	private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	public Long getOperaterId() {
		return operaterId;
	}

	public void setOperaterId(Long operaterId) {
		this.operaterId = operaterId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public double getAmountDoubleValue() {
		return null != amount ? amount.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() : 0;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ZhbRecordType getType() {
		return type;
	}

	public void setType(ZhbRecordType type) {
		this.type = type;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
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
