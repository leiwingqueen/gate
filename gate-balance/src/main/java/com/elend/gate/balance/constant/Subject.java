package com.elend.gate.balance.constant;

/**
 * 科目定义
 * @author liyongquan
 *
 */
public enum Subject {
    /**
     * 充值
     */
    CHARGE(1000, "充值"),
    WITHDRAW(1001, "提现"),
    CHARGE_FEE(1002, "充值手续费"),
    WITHDRAW_FEE(1003, "提现手续费"),
    TRANSFER_BANK(1004, "渠道提现到银行卡"),
    TRANSFER_CHANNEL(1005, "资金转入渠道"),
    /**
     * 系统调账
     */
    DATA_FIX(3000, "系统调账"),
    /**
     * 代收鉴权金额
     */
    PAY_AGENT_AUTH(1006, "代收鉴权金额"),
    /**
     * 代收鉴权手续费
     */
    PAY_AGENT_AUTH_FEE(1007, "代收鉴权手续费"),
    /**
     * 代收金额
     */
    PAY_AGENT_CHARGE(1008, "代收"),
    /**
     * 代收鉴权手续费
     */
    PAY_AGENT_CHARGE_FEE(1009, "代收手续费"),
    ;
    
    
    private int val;
    private String desc;
    
    private Subject(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }
    public int getVal() {
        return val;
    }
    public void setVal(int val) {
        this.val = val;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public static Subject from(int val)throws IllegalArgumentException {
        for (Subject one : values()) {
            if (one.getVal() == val) {
                return one;
            }
            
        }
        throw new IllegalArgumentException("illegal type:"+ val);
    }
}
