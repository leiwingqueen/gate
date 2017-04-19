package com.elend.gate.conf.facade;

import java.math.BigDecimal;

/**
 * 双乾配置
 * @author tanzl
 */
public interface MoneyMoreMoreConfig extends CallbackConfig{
    /**
     * 获取商户ID
     * @return
     */
    String getMerNo();
  
 
    /**
     *  商户前台页面跳转通知 URL.
     * @return
     */
    String getRetrunReqURL();
    
    /**
     *  商户前台页面跳转通知 URL.
     * @return
     */
    String getRequestUrl();
    
    /**
     *  商户订单查询 URL.
     * @return
     */
    String getRequestQueryUrl(); 
    
  
    /**
     * 充值费率
     * @return
     */
    BigDecimal getFeePercentage();
    

    /**
     *  商户md5key
     * @return
     */
    String getMd5Key();
    
  
}
