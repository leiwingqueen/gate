package com.elend.gate.order.constant;

import com.elend.gate.channel.constant.WithdrawStatus;


/**
 * 请求状态
 * @author liyongquan 2015年5月27日
 *
 */
public enum RequestStatus {
    REQUEST((short)1, "申请中"),
    DONE((short)2, "成功"),
    FAIL((short)3, "失败");
    private short value; 
    /**
     * 描述
     */
    private String desc;
    
    
    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private RequestStatus(short value,String desc){
        this.value=value;
        this.desc=desc;
    }
    
    public static RequestStatus from(short value) throws IllegalArgumentException{
        for (RequestStatus one : values()) {
            if (value==one.getValue())
                return one;
        }
        throw new IllegalArgumentException("illegal value：" + value);
    }
    
    /**
     * 提现状态转换
     * @param value--提现状态
     * @return
     */
    public static RequestStatus from(WithdrawStatus value){
        if(WithdrawStatus.APPLYING==value){
            return RequestStatus.REQUEST;
        }else if(WithdrawStatus.SUCCESS==value){
            return RequestStatus.DONE;
        }else{
            return RequestStatus.FAIL;
        }
    }
}
