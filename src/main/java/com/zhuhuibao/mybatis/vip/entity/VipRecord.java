package com.zhuhuibao.mybatis.vip.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.wordnik.swagger.annotations.ApiModelProperty;
/**
 * 
 * @author zhuangyuhao
 * @time   2016年9月7日 下午2:58:18
 *
 */
public class VipRecord implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3663961101942611077L;

	private Long id;

	@ApiModelProperty(value = "合同号", required = true)
	private String contractNo;

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

	@ApiModelProperty(value = "VIP级别，0：个人普通，30：个人黄金，60：个人铂金；100：企业普通，130：企业黄金，160：企业铂金", required = true)
	private int vipLevel;

	@ApiModelProperty(value = "vip级别生效时间", required = true)
	private Date activeTime;

	@ApiModelProperty(value = "vip级别失效时间", required = true)
	private Date expireTime;

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
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
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
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getVipLevel() {
		return vipLevel;
	}
	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}
	public Date getActiveTime() {
		return activeTime;
	}
	public void setActiveTime(Date activeTime) {
		this.activeTime = activeTime;
	}
	public Date getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
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
