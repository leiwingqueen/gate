package com.elend.gate.channel.exception;

/**
 * 支付失败Exception <br/>
 * lianlianwap支付同步失败不会返回参数，抛出该异常标记返回p2p
 * @author mgt
 *
 */
public class PayFailureException extends RuntimeException {
    /**
	 * 
	 */
    private static final long serialVersionUID = -6828405345879102005L;

    public PayFailureException() {

    }
    public PayFailureException(Exception e) {
        super(e);
    }

    public PayFailureException(String msg) {
        super(msg);
    }
    
    public PayFailureException(String msg, Exception e) {
        super(msg, e);
    }
}
