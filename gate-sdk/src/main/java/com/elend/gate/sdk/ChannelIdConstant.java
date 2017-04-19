package com.elend.gate.sdk;


/**
 * 支付渠道常量
 * @author liyongquan 2015年5月27日
 *
 */
public enum ChannelIdConstant {
    YEEPAY("易宝",false),
    /**连连手机端认证支付*/
    LIANLIAN_MOBILE("连连手机端认证支付",true),
    /**宝易互通*/
    UMBPAY("宝易互通", false),
    /**双乾*/
    MONEY_MORE_MORE("双乾", false),
    /**不指定银行*/
    NO_DESIGNATED("不指定渠道", false),
    /**连连WAP快捷支付*/
    LIANLIAN_WAP("连连WAP快捷支付", true),
    /**连连网银支付*/
    LIANLIAN_GATE("连连网银支付", false),
    /**宝付网银支付*/
    BAOFOO_GATE("宝付网银支付", false),
    
    ;
    
    /**
     * 描述
     */
    private String desc;
    
    /**
     * 是否认证支付
     */
    private boolean isAuthPay;
    
    
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private ChannelIdConstant(String desc,boolean isAuthPay){
        this.desc=desc;
        this.isAuthPay=isAuthPay;
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
