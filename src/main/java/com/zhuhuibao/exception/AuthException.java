package com.zhuhuibao.exception;


public class AuthException extends BaseException {

    private static final long serialVersionUID = -5776938325521340753L;

    public AuthException(Integer msgid, String message) {
        super(msgid, message);
    }


}
