package com.zhuhuibao.mybatis.oms.entity;

import java.util.Date;

public class User {
    private Integer id;

    private String username;

    private String password;

    private Date createTime;
    
    private String curLoginIp;
    
    private String curLoginTime;
    
    private String lastLoginIp;
    
    private String lastLoginTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public String getCurLoginIp() {
		return curLoginIp;
	}

	public void setCurLoginIp(String curLoginIp) {
		this.curLoginIp = curLoginIp;
	}

	public String getCurLoginTime() {
		return curLoginTime;
	}

	public void setCurLoginTime(String curLoginTime) {
		this.curLoginTime = curLoginTime;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
    
    
}