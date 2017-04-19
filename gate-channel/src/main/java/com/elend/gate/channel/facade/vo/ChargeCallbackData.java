package com.elend.gate.channel.facade.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.elend.gate.channel.constant.PayType;

/**
 * 充值回调
 * 
 * @author liyongquan 2015年5月29日
 */
public class ChargeCallbackData extends Callback {
    /**
     * 支付渠道订单号
     */
    private String channelOrderId;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 平台订单号
     */
    private String orderId;

    /**
     * 渠道的银行编号
     */
    private String channelBankId;

    /**
     * 交易支付时间
     */
    private Date payTime;

    /**
     * 交易结果通知时间
     */
    private Date noticeTime;
    /**
     * 支付网关的银行ID
     */
    private String bankId;
    
    /**
     * 协议号
     */
    private String contractNo;
    /**
     * 手续费
     */
    private BigDecimal fee;
    
    /**
     * 支付类型
     */
    private PayType payType;

    public String getChannelOrderId() {
        return channelOrderId;
    }

    public void setChannelOrderId(String channelOrderId) {
        this.channelOrderId = channelOrderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getChannelBankId() {
        return channelBankId;
    }

    public void setChannelBankId(String channelBankId) {
        this.channelBankId = channelBankId;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(Date noticeTime) {
        this.noticeTime = noticeTime;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }
}
