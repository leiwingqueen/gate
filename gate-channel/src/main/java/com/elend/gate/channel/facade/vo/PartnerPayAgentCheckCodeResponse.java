package com.elend.gate.channel.facade.vo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.channel.annotation.Param;



/**
 * 代扣校验校验码回调
 * @author mgt
 *
 */
public class PartnerPayAgentCheckCodeResponse extends PartnerBaseResponse {
    
    /**
     * 订单号
     */
    @Param(sequence=3)
    protected String orderId;
    /**
     * 金额
     */
    @Param(sequence=4)
    protected String amount;
    /**
     * 提现渠道
     */
    @Param(sequence=5)
    protected String channelId;
    /**
     * 支付渠道通知时间
     */
    @Param(sequence=6)
    protected String channelNoticeTime;
    /**
     * 手续费
     */
    @Param(sequence=7)
    protected String fee;
    
    /**
     * 校验校验码状态
     */
    @Param(sequence=8)
    private String agentStatus;

    public String getAgentStatus() {
        return agentStatus;
    }

    public void setAgentStatus(String agentStatus) {
        this.agentStatus = agentStatus;
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

    public String getChannelNoticeTime() {
        return channelNoticeTime;
    }

    public void setChannelNoticeTime(String channelNoticeTime) {
        this.channelNoticeTime = channelNoticeTime;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    @Override 
    public String toString() { 
            return ReflectionToStringBuilder.toString(this,ToStringStyle.SHORT_PREFIX_STYLE); 
    }
}
