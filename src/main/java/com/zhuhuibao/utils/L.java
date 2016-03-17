package com.zhuhuibao.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhuhuibao.service.exception.ServiceException;
import com.zhuhuibao.service.exception.UnknownServiceException;

public class L {
	private static final Logger DEFAULT_LOGGER = LoggerFactory
			.getLogger(L.class);

	public static boolean isInfoEnabled() {
		return DEFAULT_LOGGER.isInfoEnabled();
	}

	public static void info(String msg) {
		DEFAULT_LOGGER.info(msg);
	}

	public static void warn(String msg) {
		DEFAULT_LOGGER.warn(msg);
	}

	public static void error(String msg) {
		DEFAULT_LOGGER.error(msg);
	}

	public static void error(String msg, Throwable ex) {
		if (ex instanceof ServiceException) {
			ServiceException se = (ServiceException) ex;
			if (se instanceof UnknownServiceException) {
				Throwable tt = ((UnknownServiceException) se).getCause();
				if (tt != null) {
					DEFAULT_LOGGER.error(null, ex);
					error(msg, tt);
				} else if (se.getMessage() != null) {
					DEFAULT_LOGGER.error(se.getMessage());
				}
			}
		} else {
			DEFAULT_LOGGER.error(msg, ex);
		}
	}

	public static void error(Throwable ex) {
		error(null, ex);
	}

}
