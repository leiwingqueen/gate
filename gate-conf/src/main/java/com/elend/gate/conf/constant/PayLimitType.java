package com.elend.gate.conf.constant;


/**
 * 支付限额类型
 * @author mgt
 *
 */
public enum PayLimitType {
    NORMAL(1, "正常"),
    NOT_LIMIT(2, "无限制"),
    PERSON_SETTING(3, "个人设置"),
    NOT_SUPPORT(4, "不支持"),
    ;

    /**
     * 类型
     */
    private int type;
    
    /**
     * 描述
     */
    private String desc;
    
    
    
    private PayLimitType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public static PayLimitType from(int type) throws IllegalArgumentException{
        for (PayLimitType one : values()) {
            if (one.getType() == type)
                return one;
        }
        throw new IllegalArgumentException(" PayLimitType illegal type：" + type);
    }
}
