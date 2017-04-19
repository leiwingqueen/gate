package com.elend.gate.sdk.exception;

/**
 * 参数错误
 * @author mgt
 * @date 2016年8月19日
 */
public class ParamErrorException extends RuntimeException {
    /**
	 * 
	 */
    private static final long serialVersionUID = -6828405345879102005L;

    public ParamErrorException() {

    }
    public ParamErrorException(Throwable e) {
        super(e);
    }

    public ParamErrorException(String msg) {
        super(msg);
    }
    
    public ParamErrorException(String msg, Throwable e) {
        super(msg, e);
    }
}
