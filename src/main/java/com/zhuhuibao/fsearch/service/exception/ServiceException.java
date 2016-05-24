package com.zhuhuibao.fsearch.service.exception;

import java.util.Map;

public class ServiceException extends Exception {
	private String errorCode;
	private Object[] errorParams;
	private Map<?, ?> errorData;

	public ServiceException(String errorCode) {
		this.errorCode = errorCode;
	}

	public ServiceException(String errorCode, Object[] errorParams) {
		super();
		this.errorCode = errorCode;
		this.errorParams = errorParams;
	}

	public ServiceException(String errorCode, Object[] errorParams,
			Map<?, ?> errorData) {
		super();
		this.errorCode = errorCode;
		this.errorParams = errorParams;
		this.errorData = errorData;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public Object[] getErrorParams() {
		return errorParams;
	}

	public Map<?, ?> getErrorData() {
		return errorData;
	}

	public void setErrorData(Map<?, ?> errorData) {
		this.errorData = errorData;
	}

}
