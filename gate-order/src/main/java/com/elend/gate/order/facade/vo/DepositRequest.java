package com.elend.gate.order.facade.vo;

import java.math.BigDecimal;

import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.constant.PayType;
import com.elend.gate.conf.constant.PartnerConstant;

/**
 * 充值请求
 * @author liyongquan 2015年6月3日
 *
 */
public class DepositRequest {
    /**接入方*/
    private PartnerConstant partner;
    /**接入方订单号*/
    private String partnerOrderId;
    /**网关订单号*/
    private String orderId;
    /**充值金额*/
    private BigDecimal amount;
    /**回调url*/
    private String callbackUrl;
    /**点对点通知url*/
    private String notifyUrl;
    /**支付渠道*/
    private ChannelIdConstant payChannel;
    /**支付类型*/
    private PayType payType;
    
    public PartnerConstant getPartner() {
        return partner;
    }
    public void setPartner(PartnerConstant partner) {
        this.partner = partner;
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
    public String getCallbackUrl() {
        return callbackUrl;
    }
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
    public String getNotifyUrl() {
        return notifyUrl;
    }
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
    public ChannelIdConstant getPayChannel() {
        return payChannel;
    }
    public void setPayChannel(ChannelIdConstant payChannel) {
        this.payChannel = payChannel;
    }
    public PayType getPayType() {
        return payType;
    }
    public void setPayType(PayType payType) {
        this.payType = payType;
    }
}
