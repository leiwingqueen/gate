package com.elend.gate.channel.facade.vo;

import java.math.BigDecimal;

import com.elend.gate.channel.constant.BankIdConstant;
import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.constant.PayType;
import com.elend.gate.conf.constant.PartnerConstant;

/**
 * 充值相关数据
 * @author liyongquan 2015年6月1日
 *
 */
public class PartnerChargeData implements Cloneable{
    /**
     * 接入方ID
     */
    private PartnerConstant partnerId;
    /**
     * 充值金额
     */
    private BigDecimal amount;
    /**
     * 接入方订单号
     */
    private String partnerOrderId;
    /**
     * 支付渠道
     */
    private ChannelIdConstant payChannel;
    /**
     * 回调url
     */
    private String redirectUrl;
    /**
     * 点对点通知url
     */
    private String notifyUrl;
    /**
     * 银行ID(网关定义，跟具体的支付渠道无关)
     */
    private BankIdConstant bankId;
    
    /**
     * 用户ID
     */
    private long userId;
    
    /**
     * 用户请求的IP
     */
    private String ip;
    
    /**
     * 支付类型
     */
    private PayType payType;
    
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public String getPartnerOrderId() {
        return partnerOrderId;
    }
    public void setPartnerOrderId(String partnerOrderId) {
        this.partnerOrderId = partnerOrderId;
    }
    public ChannelIdConstant getPayChannel() {
        return payChannel;
    }
    public void setPayChannel(ChannelIdConstant payChannel) {
        this.payChannel = payChannel;
    }
    public String getNotifyUrl() {
        return notifyUrl;
    }
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
    public String getRedirectUrl() {
        return redirectUrl;
    }
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
    public PartnerConstant getPartnerId() {
        return partnerId;
    }
    public void setPartnerId(PartnerConstant partnerId) {
        this.partnerId = partnerId;
    }
    public BankIdConstant getBankId() {
        return bankId;
    }
    public void setBankId(BankIdConstant bankId) {
        this.bankId = bankId;
    }
    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public PayType getPayType() {
        return payType;
    }
    public void setPayType(PayType payType) {
        this.payType = payType;
    }
}
