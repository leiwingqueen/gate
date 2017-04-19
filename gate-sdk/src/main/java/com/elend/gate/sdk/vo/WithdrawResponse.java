package com.elend.gate.sdk.vo;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.sdk.WithdrawChannel;
import com.elend.gate.sdk.WithdrawStatus;


/**
 * 提现回调数据封装
 * @author mgt
 *
 */
public class WithdrawResponse {
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
    private BigDecimal amount;
    /**
     * 提现渠道
     */
    private WithdrawChannel channel;
    /**
     * 提现渠道订单号
     */
    private String channelOrderId;
    /**
     * 支付渠道通知时间
     */
    private Date channelNoticeTime;
    /**
     * 网关通知时间
     */
    private Date gateNoticeTime;
    /**
     * 手续费
     */
    private BigDecimal fee;
    
    /**
     * 提现状态
     */
    private WithdrawStatus withdrawStatus;
    
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
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public WithdrawChannel getChannel() {
        return channel;
    }
    public void setChannel(WithdrawChannel channel) {
        this.channel = channel;
    }
    public String getChannelOrderId() {
        return channelOrderId;
    }
    public void setChannelOrderId(String channelOrderId) {
        this.channelOrderId = channelOrderId;
    }
    public Date getChannelNoticeTime() {
        return channelNoticeTime;
    }
    public void setChannelNoticeTime(Date channelNoticeTime) {
        this.channelNoticeTime = channelNoticeTime;
    }
    public Date getGateNoticeTime() {
        return gateNoticeTime;
    }
    public void setGateNoticeTime(Date gateNoticeTime) {
        this.gateNoticeTime = gateNoticeTime;
    }
    public BigDecimal getFee() {
        return fee;
    }
    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }
    public WithdrawStatus getWithdrawStatus() {
        return withdrawStatus;
    }
    public void setWithdrawStatus(WithdrawStatus withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
    }
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this,
                                                  ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
