package com.elend.gate.channel.constant;

/**
 * 充值查询状态
 * @author mgt
 *
 */
public enum ChargeQueryStatus {
    /**
     * 成功
     */
    SUCCESS(1,"成功"),
    /**
     * 失败
     */
    FAILURE(2,"失败"),
    /**
     * 处理中
     */
    HANDLING(3, "处理中"),
    /**
     * 订单不存在
     */
    NOT_EXIST(4, "订单不存在"),
    /**
     * 取消
     */
    CANDELED(5, "订单不存在"),
    /**
     * 取消
     */
    UNRETURN(6, "结果未返回");
    
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

    private ChargeQueryStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static ChargeQueryStatus from(int status) {
        for (ChargeQueryStatus one : values()) {
            if (one.getStatus() == status) {
                return one;
            }
            
        }
        throw new IllegalArgumentException("illegal status:"+ status);
    }
}
