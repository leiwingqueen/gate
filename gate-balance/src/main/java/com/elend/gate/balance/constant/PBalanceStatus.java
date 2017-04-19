package com.elend.gate.balance.constant;

/**
 * 用户余额的状态
 * @author mgt
 */
public enum PBalanceStatus {
    
    /**
     * 正常
     */
    NORMAL(1, "正常"),
    
    /**
     * 锁定
     */
    LOCKED(2, "锁定");
    
    /**
     * 类型
     */
    private int status;
    
    /**
     * 描述
     */
    private String desc;
    
    private PBalanceStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static PBalanceStatus from(int status) {
        for (PBalanceStatus one : values()) {
            if (one.getStatus() == status) {
                return one;
            }
            
        }
        throw new IllegalArgumentException("illegal status:"+ status);
    }
}
