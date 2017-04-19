package com.elend.gate.order.constant;


/**
 * 通知状态
 * 1 通知中 2通知成功3通知失败'
 * @author liyongquan
 *
 */
public enum NotifyStatus {
    /**
     *通知中 
     */
    REQUEST(1, "通知中"),
    /**
     * 通知成功
     */
    SUCCESS(2, "通知成功"),
    /**
     * 通知失败
     */
    FAIL(3, "通知失败");
    
    private int status;
    private String desc;
    
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    private NotifyStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
    public static NotifyStatus from(int status)throws IllegalArgumentException {
        for (NotifyStatus one : values()) {
            if (one.getStatus() == status) {
                return one;
            }
            
        }
        throw new IllegalArgumentException("illegal status:"+ status);
    }
    
}
