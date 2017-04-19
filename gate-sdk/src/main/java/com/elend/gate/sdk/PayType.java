package com.elend.gate.sdk;


/**
 * 网银支付类型
 * @author mgt
 * @date 2016年8月19日
 */
public enum PayType {
    
    /**宝易互通*/
    PERSON(1, "个人网银"),
    ENTERPRISE(2, "企业网银"),
    ;
    
    /**
     * 类型
     */
    private int type;
    /**
     * 描述
     */
    private String desc;
    
    private PayType(int type, String desc){
        this.type = type;
        this.desc=desc;
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

    public static PayType from(String type) throws IllegalArgumentException{
        for (PayType one : values()) {
            if (one.name().equals(type))
                return one;
        }
        throw new IllegalArgumentException("PayType illegal type：" + type);
    }
}
