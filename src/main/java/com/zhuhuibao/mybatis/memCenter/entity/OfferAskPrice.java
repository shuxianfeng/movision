package com.zhuhuibao.mybatis.memCenter.entity;

public class OfferAskPrice extends OfferPrice {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//询价标题
	private String title;

	//类型名称
	private String typeName;

	private String endTime;

	private String taxName;

	private String address;

	private String categoryName;

	private String askCompanyName;

	private String askLinkMan;

	private Boolean askisShow;

	private String askTelephone;

	private String askEmail;

	private String description;

	private String productName;

	private String askContent;

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

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getAskCompanyName() {
		return askCompanyName;
	}

	public void setAskCompanyName(String askCompanyName) {
		this.askCompanyName = askCompanyName;
	}

	public String getAskLinkMan() {
		return askLinkMan;
	}

	public void setAskLinkMan(String askLinkMan) {
		this.askLinkMan = askLinkMan;
	}

	public Boolean getAskisShow() {
		return askisShow;
	}

	public void setAskisShow(Boolean askisShow) {
		this.askisShow = askisShow;
	}

	public String getAskTelephone() {
		return askTelephone;
	}

	public void setAskTelephone(String askTelephone) {
		this.askTelephone = askTelephone;
	}

	public String getAskEmail() {
		return askEmail;
	}

	public void setAskEmail(String askEmail) {
		this.askEmail = askEmail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getAskContent() {
		return askContent;
	}

	public void setAskContent(String askContent) {
		this.askContent = askContent;
	}
}
