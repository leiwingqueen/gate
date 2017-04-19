package com.elend.gate.sdk.vo;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.sdk.PayAgentChannel;




/**
 * 代扣获取验证码回调
 * @author mgt
 *
 */
public class PayAgentGetCodeResponse {
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
     * 代扣渠道
     */
    protected PayAgentChannel channel;
    /**
     * 提现渠道订单号
     */
    protected String channelOrderId;

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
    public PayAgentChannel getChannel() {
        return channel;
    }
    public void setChannel(PayAgentChannel channel) {
        this.channel = channel;
    }
    public String getChannelOrderId() {
        return channelOrderId;
    }
    public void setChannelOrderId(String channelOrderId) {
        this.channelOrderId = channelOrderId;
    }
    
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this,
                                                  ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
