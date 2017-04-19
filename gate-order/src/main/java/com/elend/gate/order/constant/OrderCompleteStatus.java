package com.elend.gate.order.constant;

import com.elend.gate.channel.constant.WithdrawStatus;


/**
 * 订单状态  对应 p_order_status_record表的状态
 * @author mgt
 *
 */
public enum OrderCompleteStatus {
    /**
     *已支付 
     */
    GATE_RECEIVE(1, "网关已接收"),
    /**
     * 被冲正
     */
    APPLYING(2, "第三方请求中"),
    /**
     * 冲正
     */
    SUCCESS(3, "成功"),
    /**
     * 冲正
     */
    FAILURE(4, "失败");
    
    private int status;
    private String desc;
    
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    private OrderCompleteStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
    public static OrderCompleteStatus from(int status)throws IllegalArgumentException {
        for (OrderCompleteStatus one : values()) {
            if (one.getStatus() == status) {
                return one;
            }
            
        }
        throw new IllegalArgumentException("illegal status:"+ status);
    }
    
    /**
     * 提现状态转换
     * @param value--提现状态
     * @return
     */
    public static OrderCompleteStatus from(WithdrawStatus value){
        if(WithdrawStatus.APPLYING==value){
            return OrderCompleteStatus.APPLYING;
        }else if(WithdrawStatus.SUCCESS==value){
            return OrderCompleteStatus.SUCCESS;
        }else{
            return OrderCompleteStatus.FAILURE;
        }
    }
    
}
