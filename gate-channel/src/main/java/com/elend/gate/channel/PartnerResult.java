package com.elend.gate.channel;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.channel.constant.PartnerResultCode;

/**
 * 这里重写一个Result主要是为了方便扩展支付渠道失败的返回码
 * @author liyongquan 2015年6月2日
 *
 * @param <T>
 */
public class PartnerResult<T> implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 4334907031092088435L;

    private static final String SUCCESS_MSG="操作成功";
    
    private static final String FAIL_MSG="操作失败";
    

    /**
     * 返回代码
     */
    private String code = PartnerResultCode.FAILURE;

    /**
     * 信息
     */
    private String message;

    /**
     * 对象
     */
    private T object;

    /**
     * 是否成功
     */
    private boolean success = false;

    public PartnerResult() {
    }
    public PartnerResult(String resultCode){
    	this(resultCode,null,PartnerResultCode.SUCCESS.equals(resultCode)?SUCCESS_MSG:FAIL_MSG);
    }
    public PartnerResult(String resultCode,T object){
    	this(resultCode,object,PartnerResultCode.SUCCESS.equals(resultCode)?SUCCESS_MSG:FAIL_MSG);
    }
    public PartnerResult(String resultCode,String message){
        this(resultCode,null,message);
    }
    public PartnerResult(String resultCode,T object,String msg){
    	this.setCode(resultCode);
    	this.setObject(object);
    	this.setMessage(msg);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        this.success = (PartnerResultCode.SUCCESS.equals(code));
        if(this.success){
            this.message=SUCCESS_MSG;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public boolean isSuccess() {
        return success;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
