package com.zhuhuibao.mybatis.oms.entity;


public class CategoryAssemble {
	private String fcateid;
	
	private String fname;
	
	private String smallIcon;
	
	private String bigIcon;
	
	private String scateid;
	
	private String sname;
	
	private String brandid ;
	
	private String brandCNName;
	
	private String brandENName;

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CategoryAssemble)
		{
			CategoryAssemble cc = (CategoryAssemble) obj;
			return this.fcateid.equals(cc.fcateid) || this.scateid.equals(cc.scateid);
		}
		return super.equals(obj);
	}

	public String getBigIcon() {
		return bigIcon;
	}

	public void setBigIcon(String bigIcon) {
		this.bigIcon = bigIcon;
	}

	public String getFcateid() {
		return fcateid;
	}

	public void setFcateid(String fcateid) {
		this.fcateid = fcateid;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getSmallIcon() {
		return smallIcon;
	}

	public void setSmallIcon(String smallIcon) {
		this.smallIcon = smallIcon;
	}

	public String getScateid() {
		return scateid;
	}

	public void setScateid(String scateid) {
		this.scateid = scateid;
	}

	public String getBrandid() {
		return brandid;
	}

	public void setBrandid(String brandid) {
		this.brandid = brandid;
	}

	public String getBrandCNName() {
		return brandCNName;
	}

	public void setBrandCNName(String brandCNName) {
		this.brandCNName = brandCNName;
	}

	public String getBrandENName() {
		return brandENName;
	}

	public void setBrandENName(String brandENName) {
		this.brandENName = brandENName;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}
	
}
