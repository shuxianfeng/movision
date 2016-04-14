package com.zhuhuibao.pojo;

public class ContractorSearchSpec {

	private String province;
	private String assetlevel;
	private String q;
	private String sort;
	private String sortorder;
	private int offset;
	private int limit;
	
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getAssetlevel() {
		return assetlevel;
	}

	public void setAssetlevel(String assetlevel) {
		this.assetlevel = assetlevel;
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
