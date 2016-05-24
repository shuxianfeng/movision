package com.zhuhuibao.fsearch.service.exception;

public class DetailedServiceException extends ServiceException {

	public DetailedServiceException(String msg) {
		super("detailed", new Object[] { msg });
		if (msg == null) {
			throw new IllegalArgumentException("Message should not be null");
		}
	}

}
