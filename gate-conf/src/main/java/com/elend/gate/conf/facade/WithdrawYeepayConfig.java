package com.elend.gate.conf.facade;



/**
 * 易宝提现配置
 * @author mgt
 *
 */
public abstract class WithdrawYeepayConfig {
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
    
    /**
     * 证书全路径文件名
     * @return
     */
    public abstract String getSecureCertificateName();
    
    /**
     * 证书密码
     * @return
     */
    public abstract String getPfxPwd();
    
    /**
     * 查询余额地址
     */
    public abstract String getQueryBalanceUrl();
}
