package com.elend.gate.channel.facade.vo;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.channel.constant.WithdrawStatus;

/**
 * 提现回调
 * @author mgt
 *
 */
public class WithdrawCallbackData extends Callback {
    /**
     * 平台订单号
     */
    private String orderId;
    /**
     * 支付渠道订单号
     */
    private String channelOrderId;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 提现结果通知时间
     */
    private Date callbackTime;
    
    /**
     * 手续费
     */
    private BigDecimal fee;
    
    /**
     * 提现状态
     */
    private WithdrawStatus withdrawStatus;
    
    /**
     * 提现请求的地址
     */
    private String requestUrl;
    
    /**
     * 消息
     */
    private String message;
    
    /**
     * 回调时应答给第大方渠道的报文
     */
    private String responseStr;
    
    /**
     * 同步返回的字符串
     */
    private String resultStr;
    
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

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

    public Date getCallbackTime() {
        return callbackTime;
    }

    public void setCallbackTime(Date callbackTime) {
        this.callbackTime = callbackTime;
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

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponseStr() {
        return responseStr;
    }

    public void setResponseStr(String responseStr) {
        this.responseStr = responseStr;
    }

    public String getResultStr() {
        return resultStr;
    }

    public void setResultStr(String resultStr) {
        this.resultStr = resultStr;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
