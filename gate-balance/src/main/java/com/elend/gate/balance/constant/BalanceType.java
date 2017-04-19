package com.elend.gate.balance.constant;

/**
 * 账本类型
 * @author mgt
 */
public enum BalanceType {
    
    /**
     * 新托管
     */
    E_COIN(5, "新托管账本", "RMB");
    
    /**
     * 类型
     */
    private int type;
    
    /**
     * 描述
     */
    private String desc;
    
    /**
     * 别名,显示用
     */
    private String alias;
    
    private BalanceType(int type, String desc, String alias) {
        this.type = type;
        this.desc = desc;
        this.alias=alias;
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

    private BalanceType(int type) {
        this.type = type;
    }
    
    public static BalanceType from(int type) {
        for (BalanceType one : values()) {
            if (one.getType() == type) {
                return one;
            }
            
        }
        throw new IllegalArgumentException("illegal type:"+ type);
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
    
}
