package com.zhuhuibao.mybatis.memberReg.entity;

public class LoginMember {
	private Long id;
    private String account;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 会员状态 0：未激活(只针对邮件)，1：注册成功，2：注销，3：完善资料，4：待认证会员，5：已认证会员
	 */
	private int status;
    private String identify;
    private String role;
    private String isexpert;
    private int ordercount;
    private int msgcount;
    private Long companyId;
    private int vipLevel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
    
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getIsexpert() {
		return isexpert;
	}

	public void setIsexpert(String isexpert) {
		this.isexpert = isexpert;
	}
    
    public int getOrdercount() {
        return ordercount;
    }

    public void setOrdercount(int ordercount) {
        this.ordercount = ordercount;
    }

    public int getMsgcount() {
        return msgcount;
    }

    public void setMsgcount(int msgcount) {
        this.msgcount = msgcount;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }
}