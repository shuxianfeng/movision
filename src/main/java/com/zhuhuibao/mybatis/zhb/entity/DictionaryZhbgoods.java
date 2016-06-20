package com.zhuhuibao.mybatis.zhb.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DictionaryZhbgoods implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7091609233482668048L;
	
	
	private Long id;
	private String name;
	private String pinyin;
	private String type;
	private String value;
	private BigDecimal price;
	private Date addTime;

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

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

}
