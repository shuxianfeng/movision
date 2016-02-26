package com.zhuhuibao.mybatis.memberReg.entity;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * 会员信息结构
 * @author penglong
 *
 */
@Alias("member")
public class Member implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	/**
	 * 账户名
	 */
	private String account;
	
	/**
	 * 会员手机号，登录账户
	 */
	private String mobile;
	
	/**
	 * 手机验证码
	 */
	private String mobileCheckCode;
	
	/**
	 * 会员邮箱，登录账户
	 */
	private String email;
	
	/**
	 * 邮箱图形验证码
	 */
	private String emailCheckCode;
	
	/**
	 * 
	 */
	private String emailCode;
	
	/**
	 * 找回密码的图片验证码
	 */
	private String checkCode;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 会员注册时间
	 */
	private String registerTime;
	
	/**
	 * 会员状态 0：未激活，1：注册成功，2：认证成功，3：vip会员
	 */
	private int status;
	
	/**
	 * 公司名称
	 */
	private String companyName;
	
	/**
	 * 会员身份 1：企业，2：个人
	 */
	private int identify;
	
	/**
	 * 公司法定代表人姓名
	 */
	private String companyCorporationName;
	
	/**
	 * 公司电话
	 */
	private String companyTelephone;
	
	/**
	 * 企业会员类型 1-厂家，2-代理商，3-工程商，4-渠道商户，5：事业单位，6：行业协会，7：政府机构，8：其他企业
	 */
	private int enterpriseMemberType;

	/**
	 * 地址
	 */
	private String address;
	
	/**
	 * 企业资质
	 */
	private String enterpriseQualification;
	
	/**
	 * 企业LOGO
	 */
	private String enterpriseLogo;
	
	/**
	 * 企业介绍
	 */
	private String enterpriseDesc;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public int getIdentify() {
		return identify;
	}

	public void setIdentify(int identify) {
		this.identify = identify;
	}

	public String getCompanyCorporationName() {
		return companyCorporationName;
	}

	public void setCompanyCorporationName(String companyCorporationName) {
		this.companyCorporationName = companyCorporationName;
	}

	public String getCompanyTelephone() {
		return companyTelephone;
	}

	public void setCompanyTelephone(String companyTelephone) {
		this.companyTelephone = companyTelephone;
	}


	public int getEnterpriseMemberType() {
		return enterpriseMemberType;
	}

	public void setEnterpriseMemberType(int enterpriseMemberType) {
		this.enterpriseMemberType = enterpriseMemberType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEnterpriseQualification() {
		return enterpriseQualification;
	}

	public void setEnterpriseQualification(String enterpriseQualification) {
		this.enterpriseQualification = enterpriseQualification;
	}

	public String getEnterpriseLogo() {
		return enterpriseLogo;
	}

	public void setEnterpriseLogo(String enterpriseLogo) {
		this.enterpriseLogo = enterpriseLogo;
	}

	public String getEnterpriseDesc() {
		return enterpriseDesc;
	}

	public void setEnterpriseDesc(String enterpriseDesc) {
		this.enterpriseDesc = enterpriseDesc;
	}

	public String getMobileCheckCode() {
		return mobileCheckCode;
	}

	public void setMobileCheckCode(String mobileCheckCode) {
		this.mobileCheckCode = mobileCheckCode;
	}

	public String getEmailCheckCode() {
		return emailCheckCode;
	}

	public void setEmailCheckCode(String emailCheckCode) {
		this.emailCheckCode = emailCheckCode;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public String getEmailCode() {
		return emailCode;
	}

	public void setEmailCode(String emailCode) {
		this.emailCode = emailCode;
	}
	
}
