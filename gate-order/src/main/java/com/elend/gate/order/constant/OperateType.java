package com.elend.gate.order.constant;

/**
 * 用户交易流水操作类型枚举：1、入 2、出
 * 
 * @author lxl
 */
public enum OperateType {

    IN((short) 1, "入"), OUT((short) 2, "出");

    private short type;

    private String desc;

    private OperateType(short type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static OperateType from(short type)throws IllegalArgumentException {
        for (OperateType one : values()) {
            if (one.getType() == type)
                return one;
        }
        throw new IllegalArgumentException("illegal type：" + type);
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
