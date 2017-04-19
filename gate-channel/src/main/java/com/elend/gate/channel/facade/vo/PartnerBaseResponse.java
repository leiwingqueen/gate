package com.elend.gate.channel.facade.vo;

import com.elend.gate.channel.annotation.Param;

/**
 * 响应基类
 * @author mgt
 *
 */
public class PartnerBaseResponse {
    /**
     * 签名
     */
    protected String hmac;
    /**
     * 接入方ID
     */
    @Param(sequence=0)
    protected String partnerId;
    /**
     * 返回消息
     */
    @Param(sequence=1)
    protected String message;
    /**
     * 接入方订单号
     */
    @Param(sequence=2)
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
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getPartnerOrderId() {
        return partnerOrderId;
    }
    public void setPartnerOrderId(String partnerOrderId) {
        this.partnerOrderId = partnerOrderId;
    }
}
