package com.elend.gate.sdk;



/**
 * 提现状态
 * @author mgt
 *
 */
public enum WithdrawStatus {
    /**
     *提现申请中
     */
    APPLYING(1, "提现申请中"),
  
    /**
     * 提现审核通过
     */
    SUCCESS(2, "提现成功"),
    /**
     * 提现资金转移失败
     */
    FAILURE(3, "提现资金转移失败");
    
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
    private WithdrawStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public static WithdrawStatus from(String name) {
        for (WithdrawStatus one : values()) {
            if (one.name().equals(name))
                return one;
        }
        throw new IllegalArgumentException("illegal name:"+ name);
    }
}
