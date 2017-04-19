package com.elend.gate.sdk.vo;


/**
 * 充值回调数据(和接入方的通讯格式定义)
 *      所有用string封装，保证传进来的拿到是原始的字符串，保证签名验证的时候不会出错
 * @author liyongquan 2015年6月8日
 *
 */
public class PartnerChargeResponse {
    /**
     * 签名
     */
    private String hmac;
    /**
     * 接入方ID
     */
    private String partnerId;
    /**
     * 返回码
     */
    private String resultCode;
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
     * 支付渠道
     */
    private String channelId;
    /**
     * 支付渠道订单号
     */
    private String channelOrderId;
    /**
     * 银行ID
     */
    private String bankId;
    /**
     * 支付时间
     */
    private String channelPayTime;
    /**
     * 支付渠道通知时间
     */
    private String channelNoticeTime;
    /**
     * 网关通知时间
     */
    private String gateNoticeTime;
    /**
     * 是否点对点通知
     */
    private String isNotify;
    
    /**
     * 合同号
     */
    private String contractNo;
    /**
     * 手续费
     */
    private String fee;
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
    public String getResultCode() {
        return resultCode;
    }
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
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
    public String getBankId() {
        return bankId;
    }
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }
    public String getChannelPayTime() {
        return channelPayTime;
    }
    public void setChannelPayTime(String channelPayTime) {
        this.channelPayTime = channelPayTime;
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
    public String getIsNotify() {
        return isNotify;
    }
    public void setIsNotify(String isNotify) {
        this.isNotify = isNotify;
    }
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    public String getFee() {
        return fee;
    }
    public void setFee(String fee) {
        this.fee = fee;
    }
}
