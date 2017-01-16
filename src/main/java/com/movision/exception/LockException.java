package com.movision.exception;


/**
 * @author zhuangyuhao
 * @since 16/6/2.
 */
public class LockException extends BaseException {

    private static final long serialVersionUID = 1L;

    public LockException(Integer msgid, String message) {
        super(msgid, message);
    }
}
