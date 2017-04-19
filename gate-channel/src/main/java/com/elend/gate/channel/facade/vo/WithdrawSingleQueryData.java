package com.elend.gate.channel.facade.vo;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.elend.gate.channel.constant.WithdrawStatus;

/**
 * 单笔单款信息查询
 * @author mgt
 */
public class WithdrawSingleQueryData {
    /**
     * 订单号
     */
    private String orderId;
    
    /**
     * 渠道订单号
     */
    private String channelOrderId;
    
    /**
     * 交易状态
     * */
    private WithdrawStatus withdrawStatus;
    
    /**
     * 交易状态描述
     * */
    private String message;
    
    /**
     * 交易金额
     */
    private BigDecimal amount;

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

    public WithdrawStatus getWithdrawStatus() {
        return withdrawStatus;
    }

    public void setWithdrawStatus(WithdrawStatus withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
