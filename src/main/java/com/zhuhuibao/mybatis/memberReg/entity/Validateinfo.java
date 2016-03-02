package com.zhuhuibao.mybatis.memberReg.entity;

import java.io.Serializable;

/**
 * 手机邮件验证信息
 * @author penglong
 *
 */
public class Validateinfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	
	private int memberId;
	
	private String mailUrl;
	
	private int valid;
	
	private String createTime;
	
	private String mobileCheckCode;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getMailUrl() {
		return mailUrl;
	}

	public void setMailUrl(String mailUrl) {
		this.mailUrl = mailUrl;
	}

	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getMobileCheckCode() {
		return mobileCheckCode;
	}

	public void setMobileCheckCode(String mobileCheckCode) {
		this.mobileCheckCode = mobileCheckCode;
	}

	@Override
	public String toString() {
		return "Validateinfo [id=" + id + ", memberId=" + memberId
				+ ", mailUrl=" + mailUrl + ", valid=" + valid + ", createTime="
				+ createTime + ", mobileCheckCode=" + mobileCheckCode + "]";
	}
	
}
