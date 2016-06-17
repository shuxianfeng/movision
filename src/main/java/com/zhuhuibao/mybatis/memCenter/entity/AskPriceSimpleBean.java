package com.zhuhuibao.mybatis.memCenter.entity;

import java.io.Serializable;

/**
 * 简单的询价信息实体类
 * @author PengLong
 *
 */
public class AskPriceSimpleBean implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
    private Long createid;
    
    private String title;

    /**
     * 询价截止日期，报价时间
     */
    private String dateTime;
    
    /**
     * 类型名称
     */
    private String typeName;
    
    /**
     * 交货地址
     */
    private String deliveryAddress;
    
    private String companyName;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCreateid() {
		return createid;
	}

	public void setCreateid(Long createid) {
		this.createid = createid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

}
