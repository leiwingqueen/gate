package com.elend.gate.conf.facade;



/**
 * 保易互通代付配置
 * @author mgt
 */
public abstract class WithdrawUmbpayConfig {
    /**
     * 获取商户ID
     * @return
     */
    public abstract String getMchtNo();
    /**
     * 获取商户私钥
     * @return
     */
    public abstract String getPwd();
    
    /**
     * 请求地址
     */
    public abstract String getRequestUril();
    
    /**
     * 手续费
     * @return
     */
    public abstract String getFixFee();
}
