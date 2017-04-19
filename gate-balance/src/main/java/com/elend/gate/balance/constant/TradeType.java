package com.elend.gate.balance.constant;

/**
 * 交易类型
 * @author mgt
 */
public enum TradeType {
    
    /**
     * 收入
     */
    IN(1, "收入"),
    
    /**
     * 支出
     */
    OUT(2, "支出");
    
    /**
     * 类型
     */
    private int type;
    
    /**
     * 描述
     */
    private String desc;
    
    private TradeType(int type, String desc) {
        this.type = type;
        this.desc = desc;
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

    private TradeType(int type) {
        this.type = type;
    }
    
    public static TradeType from(int type) {
        for (TradeType one : values()) {
            if (one.getType() == type) {
                return one;
            }
            
        }
        throw new IllegalArgumentException("illegal type:"+ type);
    }
    
}
