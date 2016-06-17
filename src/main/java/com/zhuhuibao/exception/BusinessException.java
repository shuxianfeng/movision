package com.zhuhuibao.exception;


public class BusinessException extends BaseException {

    private static final long serialVersionUID = -5776938325521340753L;

    public BusinessException(Integer msgid, String message) {
        super(msgid, message);
    }


}
