package com.elend.gate.channel.constant;


/**
 * 支付渠道常量
 * @author liyongquan 2015年5月27日
 *
 */
public enum ChannelIdConstant {
    /**易宝*/
    YEEPAY("易宝支付",false, 200),
    /**连连手机端认证支付*/
    LIANLIAN_MOBILE("连连手机端认证支付",true, 400),
    /**宝易互通*/
    UMBPAY("宝易互通支付", false, 300),
    
    /**双乾*/
    MONEY_MORE_MORE("双乾支付", false, 500),
    
    /**不指定银行*/
    NO_DESIGNATED("不指定渠道", false, 0),
    /**连连WAP快捷支付*/
    LIANLIAN_WAP("连连WAP快捷支付", true, 400),
    
    /**连连网银支付*/
    LIANLIAN_GATE("连连网银支付", false, 400),
    
    /**宝付网银支付*/
    BAOFOO_GATE("宝付网银支付", false, 700),
    
    ;
    
    /**
     * 描述
     */
    private String desc;
    /**
     * 是否认证支付
     */
    private boolean isAuthPay;
    
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

    public long getBalanceUserId() {
        return balanceUserId;
    }

    public void setBalanceUserId(long balanceUserId) {
        this.balanceUserId = balanceUserId;
    }

    private ChannelIdConstant(String desc,boolean isAuthPay, long balanceUserId){
        this.desc=desc;
        this.isAuthPay=isAuthPay;
        this.balanceUserId = balanceUserId;
    }
    
    public static ChannelIdConstant from(String type) throws IllegalArgumentException{
        for (ChannelIdConstant one : values()) {
            if (one.name().equals(type))
                return one;
        }
        throw new IllegalArgumentException("illegal type：" + type);
    }

    public boolean isAuthPay() {
        return isAuthPay;
    }

    public void setAuthPay(boolean isAuthPay) {
        this.isAuthPay = isAuthPay;
    }
}
