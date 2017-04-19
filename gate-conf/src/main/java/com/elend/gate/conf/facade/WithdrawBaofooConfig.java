package com.elend.gate.conf.facade;



/**
 * 宝付代付配置
 * @author mgt
 * @date 2016年8月31日
 */
public abstract class WithdrawBaofooConfig {
    /**
     * 获取商户ID
     * @return
     */
    public abstract String getMchtNo();
    /**
     * 获取终端编号
     * @return
     */
    public abstract String getTerminalId();
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
     * 私钥文件地址
     * @return
     */
    public abstract String getPrivateKeyFile();
    
    /**
     * 公钥文件地址
     * @return
     */
    public abstract String getPublicKeyFile();
    
    /**
     * 订单查询地址
     * @return
     */
    public abstract String getQueryRequestUril();
}
