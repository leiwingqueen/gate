package com.elend.gate.order.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class POrderPO implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 7261044440188832884L;

    /** 自增id */
    protected int id;

    /** 平台订单号 */
    protected String orderId;

    /** 交易类型:1充值,2还款,3通标投资,4vip扣费,5提现 */
    protected int orderType;

    /** 订单金额 */
    protected BigDecimal amount;

    /** 渠道（易宝） */
    protected String payChannel;

    /** 渠道订单号 */
    protected String channelTradeNo;

    /** 接入方平台 */
    protected String partnerId;

    /** 接入方订单号 */
    protected String partnerTradeNo;

    /** 银行id */
    protected String bankId;

    /** 创建时间 */
    protected Date createTime;

    /** 渠道通知完成时间， */
    protected Date chRefundTime;

    /** 通知接入方完成时间 */
    protected Date finishTime;

    /** 渠道通知金额 */
    protected BigDecimal refundAmount;

    /** 用户支付时间 */
    protected Date gmtPayment;

    /** 最后更新时间 */
    protected Date updateTime;

    /** 订单状态：1 已支付  2被冲正 3.冲正 */
    protected int status;

    /** ip地址 */
    protected String ip;

    /** 科目 */
    protected int subject;

    /** 子科目 */
    protected int subSubject;

    /** 关联id,如冲正时是关联被冲正的订单id */
    protected String refrenceId;
    /**通知状态 1.通知中 2.通知成功 3.通知失败*/
    protected int notifyStatus;
    /**手续费*/
    protected BigDecimal fee;
    

    public int getNotifyStatus() {
        return notifyStatus;
    }

    public void setNotifyStatus(int notifyStatus) {
        this.notifyStatus = notifyStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getChannelTradeNo() {
        return channelTradeNo;
    }

    public void setChannelTradeNo(String channelTradeNo) {
        this.channelTradeNo = channelTradeNo;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerTradeNo() {
        return partnerTradeNo;
    }

    public void setPartnerTradeNo(String partnerTradeNo) {
        this.partnerTradeNo = partnerTradeNo;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getChRefundTime() {
        return chRefundTime;
    }

    public void setChRefundTime(Date chRefundTime) {
        this.chRefundTime = chRefundTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Date getGmtPayment() {
        return gmtPayment;
    }

    public void setGmtPayment(Date gmtPayment) {
        this.gmtPayment = gmtPayment;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public int getSubSubject() {
        return subSubject;
    }

    public void setSubSubject(int subSubject) {
        this.subSubject = subSubject;
    }

    public String getRefrenceId() {
        return refrenceId;
    }

    public void setRefrenceId(String refrenceId) {
        this.refrenceId = refrenceId;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this,
                                                  ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
