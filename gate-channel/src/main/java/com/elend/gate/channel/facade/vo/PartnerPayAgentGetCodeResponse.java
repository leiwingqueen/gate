package com.elend.gate.channel.facade.vo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.channel.annotation.Param;



/**
 * 代扣获取验证码回调
 * @author mgt
 *
 */
public class PartnerPayAgentGetCodeResponse extends PartnerBaseResponse {
    
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

    @Override 
    public String toString() { 
            return ReflectionToStringBuilder.toString(this,ToStringStyle.SHORT_PREFIX_STYLE); 
    }
}
