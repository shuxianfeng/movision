package com.movision.common;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * controller返回的实体：封装了返回前台的json数据
 * 
 * @author pl
 */
@ApiModel(value = "后台返回前端的统一JSON格式", description = "JSON格式定义")
public class Response implements Serializable {

	private static final long serialVersionUID = 4273005680206220420L;
	/**
	 * 返回结果码 200成功，400失败，401没有权限
	 */
	@ApiModelProperty(value = "返回结果码,200成功，400失败，401没有权限", required = true)
	private int code = 200;
	/**
	 * 操作结果信息
	 */
	@ApiModelProperty(value = "返回信息", notes = "异常返回信息")
	private String message = "";
	/**
	 * 返回的数据
	 */
	@ApiModelProperty(value = "返回数据", required = true)
	private Object data;

	/**
	 * 平台返回的消息码
	 * 
	 * @return
	 */
	@ApiModelProperty(value = "平台返回的消息码")
	private int msgCode;

	public Response() {

	}

	public Response(String message) {
		this.message = message;
	}

	public Response(Object data){
		this.data = data;
	}

	public Response(int code, String message, Object data, int msgCode) {
		this.code = code;
		this.message = message;
		this.data = data;
		this.msgCode = msgCode;
	}

	public int getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(int msgCode) {
		this.msgCode = msgCode;
	}

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
		if (data == null) {
			data = new Object[] {};
		}
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
