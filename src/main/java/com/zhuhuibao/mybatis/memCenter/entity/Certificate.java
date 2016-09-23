package com.zhuhuibao.mybatis.memCenter.entity;

import org.apache.commons.lang3.StringUtils;

public class Certificate {
	private Integer id;

	private String name;

	private String type;

	private String degree;

	private int searchFlag;

	private int weight;

	private int category;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public int getSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(int searchFlag) {
		this.searchFlag = searchFlag;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String[] getDegreeArray() {
		return StringUtils.isNotBlank(degree) ? degree.split(",") : new String[] {};
	}

}