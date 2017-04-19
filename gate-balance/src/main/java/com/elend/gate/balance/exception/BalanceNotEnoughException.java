package com.elend.gate.balance.exception;

/**
 * 余额不足异常
 * @author mgt
 *
 */
public class BalanceNotEnoughException extends BalanceException {

    private static final long serialVersionUID = 6331193860773389884L;

    public BalanceNotEnoughException(){
        
    }
    
    public BalanceNotEnoughException(Throwable cause){
        super(cause);
    }
    
    public BalanceNotEnoughException(String msg){
        super(msg);
    }
    
    public BalanceNotEnoughException(String message, Throwable cause) {
        super(message, cause);
    }
}
