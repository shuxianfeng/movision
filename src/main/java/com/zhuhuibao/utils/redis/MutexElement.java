package com.zhuhuibao.utils.redis;

/**
 * 互斥对象 
 * @author zhuangyuhao
 * @time   2016年10月19日 上午11:53:32
 *
 */
public class MutexElement {
	
	private String type;
	
	private String businessNo;
	
	private String businessDesc;
	
	private int time;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBusinessNo() {
		return businessNo;
	}

	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}

	public String getBusinessDesc() {
		return businessDesc;
	}

	public void setBusinessDesc(String businessDesc) {
		this.businessDesc = businessDesc;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public MutexElement(String type, String businessNo, String businessDesc,
			int time) {
		super();
		this.type = type;
		this.businessNo = businessNo;
		this.businessDesc = businessDesc;
		this.time = time;
	}

	
	
}
