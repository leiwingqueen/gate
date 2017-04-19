package com.elend.gate.sdk.vo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 提现回调
 * @author mgt
 *
 */
public class PartnerWithdrawResponse {
    /**
     * 签名
     */
    private String hmac;
    /**
     * 接入方ID
     */
    private String partnerId;
    /**
     * 返回消息
     */
    private String message;
    /**
     * 接入方订单号
     */
    private String partnerOrderId;
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 金额
     */
    private String amount;
    /**
     * 提现渠道
     */
    private String channelId;
    /**
     * 提现渠道订单号
     */
    private String channelOrderId;
    /**
     * 支付渠道通知时间
     */
    private String channelNoticeTime;
    /**
     * 网关通知时间
     */
    private String gateNoticeTime;
    /**
     * 手续费
     */
    private String fee;
    
    /**
     * 提现状态
     */
    protected String withdrawStatus;
    
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
    public String getChannelNoticeTime() {
        return channelNoticeTime;
    }
    public void setChannelNoticeTime(String channelNoticeTime) {
        this.channelNoticeTime = channelNoticeTime;
    }
    public String getGateNoticeTime() {
        return gateNoticeTime;
    }
    public void setGateNoticeTime(String gateNoticeTime) {
        this.gateNoticeTime = gateNoticeTime;
    }
    public String getFee() {
        return fee;
    }
    public void setFee(String fee) {
        this.fee = fee;
    }
    public String getWithdrawStatus() {
        return withdrawStatus;
    }
    public void setWithdrawStatus(String withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
    }
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this,
                                                  ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
