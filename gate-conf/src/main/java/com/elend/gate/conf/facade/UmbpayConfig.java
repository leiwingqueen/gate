package com.elend.gate.conf.facade;

import java.math.BigDecimal;

/**
 * 保易互通配置
 * @author mgt
 */
public interface UmbpayConfig extends CallbackConfig{
    /**
     * 获取商户ID
     * @return
     */
    String getMerId();
    /**
     * 获取商户私钥
     * @return
     */
    String getPriKey();
    
    String getUmbpayCommonReqURL();
    /**
     * 充值订单查询的URL
     * @return
     */
    String getQueryChargeReqURL();
    /**
     * 同步返回URL
     * @return
     */
    String getRedirectUrl();
    /**
     * 充值费率
     * @return
     */
    BigDecimal getFeePercentage();
}
