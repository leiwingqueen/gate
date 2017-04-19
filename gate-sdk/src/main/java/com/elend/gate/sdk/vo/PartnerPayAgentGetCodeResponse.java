package com.elend.gate.sdk.vo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;




/**
 * 代扣获取验证码回调
 * @author mgt
 *
 */
public class PartnerPayAgentGetCodeResponse {
    
    /**
     * 签名
     */
    protected String hmac;
    /**
     * 接入方ID
     */
    protected String partnerId;
    /**
     * 返回消息
     */
    protected String message;
    /**
     * 接入方订单号
     */
    protected String partnerOrderId;
    /**
     * 订单号
     */
    protected String orderId;
    /**
     * 金额
     */
    protected String amount;
    /**
     * 代扣渠道
     */
    protected String channelId;
    /**
     * 提现渠道订单号
     */
    protected String channelOrderId;
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
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getChannelId() {
        return channelId;
    }
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
    public String getChannelOrderId() {
        return channelOrderId;
    }
    public void setChannelOrderId(String channelOrderId) {
        this.channelOrderId = channelOrderId;
    }
    
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this,
                                                  ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
