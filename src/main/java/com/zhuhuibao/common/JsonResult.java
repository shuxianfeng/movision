package com.zhuhuibao.common;

import java.io.Serializable;
/**
 * 返回前台的json数据
 * @author coding云
 * @date 2014-5-29 15:36:59
 */
public class JsonResult implements Serializable{

	private static final long serialVersionUID = 4273005680206220420L;
	/**
	 * 返回结果码
	 * 200成功，400失败，403没有权限
	 */
	private int code = 200;
	/**
	 * 操作结果信息
	 */
	private String message = "";
	/**
	 * 返回的数据
	 */
	private Object data;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		if(data == null)
		{
			Object[] obj = {};
			data = obj;
		}
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}
