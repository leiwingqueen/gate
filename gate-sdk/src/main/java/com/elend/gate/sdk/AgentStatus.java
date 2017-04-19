package com.elend.gate.sdk;



/**
 * 代收的结果状态
 * @author longguangqing
 *
 */
public enum AgentStatus {
    /**
     * 已接收
     */
    RECEIVE(1,"处理中"),
    /**
     * 成功
     */
    SUCCESS(2, "成功"),
    /**
     * 失败
     */
    FAILURE(3,"失败");
    
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

    private AgentStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static AgentStatus from(int status) {
        for (AgentStatus one : values()) {
            if (one.getStatus() == status) {
                return one;
            }
            
        }
        throw new IllegalArgumentException("illegal status:"+ status);
    }
}
