package com.elend.gate.order.constant;



/**
 * 交易类型:1充值
 * @author liyongquan
 *
 */
public enum OrderType {
    /**
     * 充值
     */
    CHARGE(1,"充值"),
    WITHDRAW(2, "提现"),
    CHARGE_FEE(3, "充值手续费"),
    WITHDRAW_FEE(4, "提现手续费"),
    TRANSFER(5, "转账"),
    /**
     * 系统调账
     */
    DATA_FIX(6, "系统调账"),
    /**
     * 代收鉴权
     */
    PAY_AGENT_AUTH(7, "代收鉴权"),
    /**
     * 代收鉴权手续费
     */
    PAY_AGENT_AUTH_FEE(8, "代收鉴权手续费"),
    /**
     * 代收
     */
    PAY_AGENT_CHARGE(9, "代收"),
    /**
     * 代收手续费
     */
    PAY_AGENT_CHARGE_FEE(10, "代收手续费"),
    ;
    
    private int type;
    private String desc;
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getDesc() {
        return desc;
    }
    private OrderType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public static OrderType from(int type)throws IllegalArgumentException {
        for (OrderType one : values()) {
            if (one.getType() == type) {
                return one;
            }
            
        }
        throw new IllegalArgumentException("illegal type:"+ type);
    }
}
