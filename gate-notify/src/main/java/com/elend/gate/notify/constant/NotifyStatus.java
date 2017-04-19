package com.elend.gate.notify.constant;


/**
 * 通知状态
 * @author liyongquan 2015年5月27日
 *
 */
public enum NotifyStatus {
    SUCCESS(1,"成功"),
    FAIL(2,"失败");
    private int value;
    /**
     * 描述
     */
    private String desc;
    
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private NotifyStatus(int value,String desc){
        this.value=value;
        this.desc=desc;
    }
    
    public static NotifyStatus from(int value) throws IllegalArgumentException{
        for (NotifyStatus one : values()) {
            if (one.name().equals(value))
                return one;
        }
        throw new IllegalArgumentException("illegal type：" + value);
    }
}
