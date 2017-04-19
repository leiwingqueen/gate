package com.elend.gate.conf.facade;

import com.elend.gate.conf.constant.PartnerConstant;

/**
 * 接入方配置
 * @author liyongquan
 *
 */
public interface PartnerConfig {
    /**
     * 获取接入方私钥
     * @param partnerId--接入方ID
     * @return
     */
    String getPriKey(PartnerConstant partnerId);
}
