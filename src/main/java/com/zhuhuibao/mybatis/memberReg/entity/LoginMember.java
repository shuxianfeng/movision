package com.zhuhuibao.mybatis.memberReg.entity;

public class LoginMember {
	private Long id;
    private String account;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 会员状态 0：未激活，1：注册成功，2：认证成功，3：vip会员
	 */
	private int status;
    private String identifyname;
    private String rolename;
    private int ordercount;
    private int msgcount;

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
    
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIdentifyname() {
		return identifyname;
	}

	public void setIdentifyname(String identifyname) {
		this.identifyname = identifyname;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
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

}