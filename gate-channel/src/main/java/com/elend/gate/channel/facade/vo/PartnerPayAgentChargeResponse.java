package com.elend.gate.channel.facade.vo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.channel.annotation.Param;



/**
 * 代扣返回参数
 * @author mgt
 *
 */
public class PartnerPayAgentChargeResponse extends PartnerBaseResponse {
    
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
     * 提现渠道订单号
     */
    @Param(sequence=6)
    protected String channelOrderId;
    /**
     * 支付渠道通知时间
     */
    @Param(sequence=7)
    protected String channelNoticeTime;
    /**
     * 网关通知时间
     */
    @Param(sequence=8)
    protected String gateNoticeTime;
    /**
     * 手续费
     */
    @Param(sequence=9)
    protected String fee;
    
    /**
     * 校验校验码状态
     */
    @Param(sequence=10)
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

    public String getChannelOrderId() {
        return channelOrderId;
    }

    public void setChannelOrderId(String channelOrderId) {
        this.channelOrderId = channelOrderId;
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
