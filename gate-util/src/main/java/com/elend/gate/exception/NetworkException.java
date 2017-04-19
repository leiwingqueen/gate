package com.elend.gate.exception;

public class NetworkException extends RuntimeException{
    /**
     * 
     */
    private static final long serialVersionUID = 3816560129097951959L;
    private String message="网络错误";
    
    public NetworkException(String message){
        this.message=message;
    }
    
    public NetworkException(){}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
