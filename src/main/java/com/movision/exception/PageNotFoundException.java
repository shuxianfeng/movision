package com.movision.exception;


public class PageNotFoundException extends BaseException {

    private static final long serialVersionUID = -5776938325521340753L;

    public PageNotFoundException(Integer msgid, String message) {
        super(msgid, message);
    }

}
