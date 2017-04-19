package com.elend.gate.channel.constant;


/**
 * 银行卡类型（对公或对私）
 * @author mgt
 *
 */
public enum BankAccountType {
    /**宝易互通*/
    PUBLIC("对公"),
    PRIVATE("对私"),
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

    private BankAccountType(String desc){
        this.desc=desc;
    }

    public static BankAccountType from(String type) throws IllegalArgumentException{
        for (BankAccountType one : values()) {
            if (one.name().equals(type))
                return one;
        }
        throw new IllegalArgumentException("BankAccountType illegal type：" + type);
    }
}
