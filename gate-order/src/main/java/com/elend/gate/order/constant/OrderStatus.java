package com.elend.gate.order.constant;


/**
 * 订单状态
 * 1 已支付 2被冲正3 冲正'
 * @author liyongquan
 *
 */
public enum OrderStatus {
    /**
     *已支付 
     */
    PAYED(1, "已支付"),
    /**
     * 被冲正
     */
    FLUSHED(2, "被冲正"),
    /**
     * 冲正
     */
    FLUSH(3, "冲正");
    
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
    private OrderStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
    public static OrderStatus from(int status)throws IllegalArgumentException {
        for (OrderStatus one : values()) {
            if (one.getStatus() == status) {
                return one;
            }
            
        }
        throw new IllegalArgumentException("illegal status:"+ status);
    }
    
}
