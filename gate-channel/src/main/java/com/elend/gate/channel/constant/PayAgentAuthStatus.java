package com.elend.gate.channel.constant;



/**
 * 代扣认证状态
 * @author mgt
 *
 */
public enum PayAgentAuthStatus {
    /**
     *获取校验码
     */
    GET_CODE(1, "获取校验码"),
  
    /**
     * 获取校验码成功
     */
    GET_CODE_SUCCESS(2, "获取校验码成功"),
    /**
     * 获取校验码失败
     */
    GET_CODE_FAILURE(3, "获取交谈吗失败"),
    /**
     * 认证中
     */
    CHECK_CODE(4, "认证中"),
    /**
     * 认证成功
     */
    CHECK_CODE_SUCCESS(5, "认证成功"),
    /**
     * 认证失败
     */
    CHECK_CODE_FAILURE(6, "认证失败");
    
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
    private PayAgentAuthStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public static PayAgentAuthStatus from(int status) {
        for (PayAgentAuthStatus one : values()) {
            if (one.getStatus() == status) {
                return one;
            }
        }
        throw new IllegalArgumentException("PayAgentAuthStatus illegal status:"+ status);
    }
}
