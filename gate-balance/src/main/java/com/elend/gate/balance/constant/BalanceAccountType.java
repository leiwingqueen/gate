package com.elend.gate.balance.constant;

/**
 * 账本账户的类型
 * @author mgt
 *
 */
public enum BalanceAccountType {
    
    /**
     * 总账
     */
    TOTAL_ACCOUNT(1, "总账"),
    /**
     * 渠道分账
     */
    CHANNEL_ACCOUNT(2, "渠道分账"),
    /**
     * 银行卡
     */
    BANK_ACCOUNT(3, "银行卡");
    
    /**
     * 类型
     */
    private int type;
    
    /**
     * 描述
     */
    private String desc;
    
    private BalanceAccountType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private BalanceAccountType(int type) {
        this.type = type;
    }
    
    public static BalanceAccountType from(int type) {
        for (BalanceAccountType one : values()) {
            if (one.getType() == type) {
                return one;
            }
            
        }
        throw new IllegalArgumentException("illegal type:"+ type);
    }
}
