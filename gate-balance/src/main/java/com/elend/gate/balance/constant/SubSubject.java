/**
 * 
 */
package com.elend.gate.balance.constant;

/**
 * @author linshumao
 * 
 *
 */
public enum SubSubject {
    
    /**
     * 充值
     */
    CHARGE(1001, "充值"),
    WITHDRAW(1002, "提现"),
    CHARGE_FEE(1003, "充值手续费"),
    WITHDRAW_FEE(1004, "提现手续费"),
    TRANSFER_BANK(1005, "渠道提现到银行卡"),
    TRANSFER_CHANNEL(1006, "资金转入渠道"),
    /**
     * 系统调账
     */
    DATA_FIX(3001, "系统调账"),
    /**
     * 代收鉴权金额
     */
    PAY_AGENT_AUTH(1007, "代收鉴权金额"),
    /**
     * 代收鉴权手续费
     */
    PAY_AGENT_AUTH_FEE(1008, "代收鉴权手续费"),
    /**
     * 代收金额
     */
    PAY_AGENT_CHARGE(1009, "代收"),
    /**
     * 代收鉴权手续费
     */
    PAY_AGENT_CHARGE_FEE(1010, "代收手续费"),
    ;
    
    private int val;
    private String desc;
    
    private SubSubject(int val, String desc) {
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
    
    public static SubSubject from(int val)throws IllegalArgumentException {
        for (SubSubject one : values()) {
            if (one.getVal() == val) {
                return one;
            }
            
        }
        throw new IllegalArgumentException("illegal type:"+ val);
    }
}
