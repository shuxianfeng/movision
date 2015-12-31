package com.zhuhuibao.utils.exception;


import org.apache.shiro.authc.AuthenticationException;

public class SmsException extends AuthenticationException {

    public SmsException() {

        super();

    }

    public SmsException(String message, Throwable cause) {

        super(message, cause);

    }

    public SmsException(String message) {

        super(message);

    }

    public SmsException(Throwable cause) {

        super(cause);

    }

}
