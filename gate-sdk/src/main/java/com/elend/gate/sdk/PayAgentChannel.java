package com.elend.gate.sdk;


/**
 * 代扣渠道
 * @author mgt
 *
 */
public enum PayAgentChannel {
    /**宝易互通*/
    UMBPAY_PAY_AGENT("宝易互通代扣", 301);
    
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

    private PayAgentChannel(String desc, long balanceUserId){
        this.desc=desc;
        this.balanceUserId = balanceUserId;
    }
    
    public long getBalanceUserId() {
        return balanceUserId;
    }

    public void setBalanceUserId(long balanceUserId) {
        this.balanceUserId = balanceUserId;
    }

    public static PayAgentChannel from(String type) throws IllegalArgumentException{
        for (PayAgentChannel one : values()) {
            if (one.name().equals(type))
                return one;
        }
        throw new IllegalArgumentException("PayAgentChannel illegal type：" + type);
    }
}
