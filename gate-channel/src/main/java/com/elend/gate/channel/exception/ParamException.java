package com.elend.gate.channel.exception;

/**
 * 参数不能为空
 * 
 * @author liyongquan
 */
public class ParamException extends RuntimeException {
    /**
	 * 
	 */
    private static final long serialVersionUID = -6828405345879102005L;

    public ParamException() {

    }
    public ParamException(Exception e) {
        super(e);
    }

    public ParamException(String msg) {
        super(msg);
    }
}
