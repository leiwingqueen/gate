package com.elend.gate.conf.facade;

import java.math.BigDecimal;

/**
 * 易宝的配置属性
 * @author liyongquan 2015年5月21日
 *
 */
public interface YeepayConfig extends CallbackConfig{
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
    
    String getOnlinePaymentReqURL();
    String getYeepayCommonReqURL();
    String getQueryRefundReqURL();
    /**
     * 充值订单查询的URL
     * @return
     */
    String getQueryChargeReqURL();
    /**
     * 充值费率
     * @return
     */
    BigDecimal getFeePercentage();
    /**
     * 企业充值费率
     * @return
     */
    public BigDecimal getFeeEnterprise();
}
