package com.elend.gate.channel.facade.vo;

import com.elend.gate.channel.annotation.Param;

/**
 * 提现请求参数
 * @author mgt
 *
 */
public class PartnerBaseRequest {
    /**
     * 签名
     */
    @Param(required=true)
    protected String hmac;
    /**
     * 借接入方ID
     */
    @Param(required=true, sequence=0)
    protected String partnerId;
    
    /**
     * 接入方订单号
     */
    @Param(required=true, sequence=1)
    protected String partnerOrderId;
    
    public String getHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerOrderId() {
        return partnerOrderId;
    }

    public void setPartnerOrderId(String partnerOrderId) {
        this.partnerOrderId = partnerOrderId;
    }
}
