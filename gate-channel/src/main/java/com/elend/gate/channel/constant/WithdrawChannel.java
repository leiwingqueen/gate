package com.elend.gate.channel.constant;


/**
 * 代付渠道
 * @author mgt
 *
 */
public enum WithdrawChannel {
    /**易宝*/
    YEEPAY_WITHDRAW("易宝提现", 200),
    /**宝易互通*/
    UMBPAY_WITHDRAW("宝易互通提现", 301),
    /**宝付支付*/
    BAOFOO_WITHDRAW("宝付支付提现", 700),
    
    ;
    
    /**
     * 描述
     */
    private String desc;
    /**
     * 账本的userId
     */
    private long balanceUserId;
    
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private WithdrawChannel(String desc, long balanceUserId){
        this.desc=desc;
        this.balanceUserId = balanceUserId;
    }
    
    public long getBalanceUserId() {
        return balanceUserId;
    }

    public void setBalanceUserId(long balanceUserId) {
        this.balanceUserId = balanceUserId;
    }

    public static WithdrawChannel from(String type) throws IllegalArgumentException{
        for (WithdrawChannel one : values()) {
            if (one.name().equals(type))
                return one;
        }
        throw new IllegalArgumentException("illegal type：" + type);
    }
}
