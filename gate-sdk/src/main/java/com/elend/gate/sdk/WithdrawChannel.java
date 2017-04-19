package com.elend.gate.sdk;


/**
 * 代付渠道
 * @author mgt
 *
 */
public enum WithdrawChannel {
    /**易宝*/
    YEEPAY_WITHDRAW("易宝"),
    /**宝易互通*/
    UMBPAY_WITHDRAW("宝易互通"),
    /**宝付支付*/
    BAOFOO_WITHDRAW("宝付支付提现"),
    ;
    
    /**
     * 描述
     */
    private String desc;
    
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private WithdrawChannel(String desc){
        this.desc=desc;
    }
    
    public static WithdrawChannel from(String type) throws IllegalArgumentException{
        for (WithdrawChannel one : values()) {
            if (one.name().equals(type))
                return one;
        }
        throw new IllegalArgumentException("illegal type：" + type);
    }
}
