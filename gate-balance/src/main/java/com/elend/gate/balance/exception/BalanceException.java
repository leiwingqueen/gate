package com.elend.gate.balance.exception;

/**
 * 余额操作异常
 * @author mgt
 *
 */
public class BalanceException extends RuntimeException {

    private static final long serialVersionUID = -6962282149774484002L;

    public BalanceException(){
        
    }
    
    public BalanceException(Throwable cause){
        super(cause);
    }
    
    public BalanceException(String msg){
        super(msg);
    }
    
    public BalanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
