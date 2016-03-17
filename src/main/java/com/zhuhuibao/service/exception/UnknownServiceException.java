package com.zhuhuibao.service.exception;


public class UnknownServiceException extends ServiceException {

	public UnknownServiceException(Throwable t) {
		super("unknown");
		this.initCause(t);
	}

	public UnknownServiceException(String s) {
		super("unknown");
		this.initCause(new RuntimeException(s));
	}

}
