package com.elend.gate.sdk.exception;

/**
 * 签名校验失败
 * 
 * @author liyongquan
 */
public class CheckSignatureException extends RuntimeException {
    /**
	 * 
	 */
    private static final long serialVersionUID = -6828405345879102005L;

    public CheckSignatureException() {

    }
    public CheckSignatureException(Exception e) {
        super(e);
    }

    public CheckSignatureException(String msg) {
        super(msg);
    }
}
