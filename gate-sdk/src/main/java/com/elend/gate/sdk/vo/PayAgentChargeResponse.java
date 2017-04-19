package com.elend.gate.sdk.vo;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.sdk.AgentStatus;
import com.elend.gate.sdk.PayAgentChannel;



/**
 * 代收回调参数
 * @author mgt
 *
 */
public class PayAgentChargeResponse {
    
    /**
     * 接入方ID
     */
    protected String partnerId;
    /**
     * 返回消息
     */
    protected String message;
    /**
     * 接入方订单号
     */
    protected String partnerOrderId;
    /**
     * 订单号
     */
    protected String orderId;
    /**
     * 金额
     */
    protected BigDecimal amount;
    /**
     * 提现渠道
     */
    protected PayAgentChannel channel;
    /**
     * 提现渠道订单号
     */
    protected String channelOrderId;
    /**
     * 支付渠道通知时间
     */
    protected Date channelNoticeTime;
    /**
     * 网关通知时间
     */
    protected Date gateNoticeTime;
    /**
     * 手续费
     */
    protected BigDecimal fee;
    
    /**
     * 校验校验码状态
     */
    private AgentStatus agentStatus;


    public String getPartnerId() {
        return partnerId;
    }


    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
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

    public BigDecimal getAmount() {
        return amount;
    }


    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public String getChannelOrderId() {
        return channelOrderId;
    }


    public void setChannelOrderId(String channelOrderId) {
        this.channelOrderId = channelOrderId;
    }

    public Date getChannelNoticeTime() {
        return channelNoticeTime;
    }


    public void setChannelNoticeTime(Date channelNoticeTime) {
        this.channelNoticeTime = channelNoticeTime;
    }


    public Date getGateNoticeTime() {
        return gateNoticeTime;
    }


    public void setGateNoticeTime(Date gateNoticeTime) {
        this.gateNoticeTime = gateNoticeTime;
    }

    public BigDecimal getFee() {
        return fee;
    }


    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }


    public AgentStatus getAgentStatus() {
        return agentStatus;
    }


    public void setAgentStatus(AgentStatus agentStatus) {
        this.agentStatus = agentStatus;
    }


    public PayAgentChannel getChannel() {
        return channel;
    }


    public void setChannel(PayAgentChannel channel) {
        this.channel = channel;
    }


    @Override 
    public String toString() { 
            return ReflectionToStringBuilder.toString(this,ToStringStyle.SHORT_PREFIX_STYLE); 
    }
}
