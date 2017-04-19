package com.elend.gate.channel.facade.vo;

import com.elend.gate.channel.annotation.Param;

/**
 * 充值请求数据(和接入方的通讯格式定义)
 *      所有用string封装，保证传进来的拿到是原始的字符串，保证签名验证的时候不会出错
 * @author liyongquan 2015年6月5日
 *
 */
public class PartnerChargeRequest {
    /**
     * 签名
     */
    @Param(required=true)
    private String hmac;
    /**
     * 接入方ID
     */
    @Param(required=true,sequence=0)
    private String partnerId;
    /**
     * 接入方订单号
     */
    @Param(required=true,sequence=1)
    private String partnerOrderId;
    /**
     * 金额
     */
    @Param(required=true,sequence=2)
    private String amount;
    /**
     * 支付渠道
     */
    @Param(required=false,sequence=3)
    private String payChannel;
    /**
     * 回调url
     */
    @Param(required=true,sequence=4)
    private String redirectUrl;
    /**
     * 点对点通知url
     */
    @Param(required=true,sequence=5)
    private String notifyUrl;
    /**
     * 银行ID(网关定义，跟具体的支付渠道无关)
     */
    @Param(required=false,sequence=6)
    private String bankId;
    
    /**
     * 时间戳
     */
    @Param(required=true,sequence=7)
    private String timeStamp;
    
    @Param(required=true,sequence=8)
    private String ip;
    
    /**
     * 用户ID
     */
    @Param(required=false,sequence=9)
    private String userId;
    
    /**
     * 用户ID
     */
    @Param(required=false,sequence=10)
    private String payType;
   
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
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getPayChannel() {
        return payChannel;
    }
    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }
    public String getRedirectUrl() {
        return redirectUrl;
    }
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
    public String getNotifyUrl() {
        return notifyUrl;
    }
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
    public String getPartnerOrderId() {
        return partnerOrderId;
    }
    public void setPartnerOrderId(String partnerOrderId) {
        this.partnerOrderId = partnerOrderId;
    }
    public String getBankId() {
        return bankId;
    }
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }
    public String getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
}
