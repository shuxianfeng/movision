package com.zhuhuibao.pojo;

public class ProductSearchSpec {
	private int fcateid;
	private int scateid;
	private int brandid;
	private int member_companyIdentify;
	private String q;
	private String sort;
	private String sortorder;
	private int offset;
	private int limit;
	
	public int getFcateid() {
		return fcateid;
	}

	public void setFcateid(int fcateid) {
		this.fcateid = fcateid;
	}

	public int getScateid() {
		return scateid;
	}

	public void setScateid(int scateid) {
		this.scateid = scateid;
	}

	public int getBrandid() {
		return brandid;
	}

	public void setBrandid(int brandid) {
		this.brandid = brandid;
	}
	
	public int getMember_companyIdentify() {
		return member_companyIdentify;
	}

	public void setMember_companyIdentify(int member_companyIdentify) {
		this.member_companyIdentify = member_companyIdentify;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public String getSortorder() {
		return sortorder;
	}

	public void setSortorder(String sortorder) {
		this.sortorder = sortorder;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
