package com.elend.gate.balance.constant;

/**
 * 余额变动流水状态
 * @author mgt
 */
public enum PUserBalanceLogStatus {
    
    /**
     * 正常
     */
    NORMAL(1, "正常");
    
    /**
     * 类型
     */
    private int status;
    
    /**
     * 描述
     */
    private String desc;
    
    private PUserBalanceLogStatus(int status, String desc) {
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

    public static PUserBalanceLogStatus from(int status) {
        for (PUserBalanceLogStatus one : values()) {
            if (one.getStatus() == status) {
                return one;
            }
            
        }
        throw new IllegalArgumentException("illegal status:"+ status);
    }
}
