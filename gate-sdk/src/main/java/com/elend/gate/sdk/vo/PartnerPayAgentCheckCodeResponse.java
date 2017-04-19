package com.elend.gate.sdk.vo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;



/**
 * 代扣校验校验码回调
 * @author mgt
 *
 */
public class PartnerPayAgentCheckCodeResponse {
    
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
     * 提现渠道
     */
    protected String channelId;
    /**
     * 支付渠道通知时间
     */
    protected String channelNoticeTime;
    /**
     * 手续费
     */
    protected String fee;
    
    /**
     * 校验校验码状态
     */
    private String agentStatus;

    public String getAgentStatus() {
        return agentStatus;
    }

    public void setAgentStatus(String agentStatus) {
        this.agentStatus = agentStatus;
    }

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

    public String getChannelNoticeTime() {
        return channelNoticeTime;
    }

    public void setChannelNoticeTime(String channelNoticeTime) {
        this.channelNoticeTime = channelNoticeTime;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    @Override 
    public String toString() { 
            return ReflectionToStringBuilder.toString(this,ToStringStyle.SHORT_PREFIX_STYLE); 
    }
}
