package com.elend.gate.channel.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CBankIdConfigPO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3172948880560497350L;

    /** 流水号 */
    protected int id;

    /** 支付渠道ID */
    protected String payChannel;

    /** 渠道银行ID */
    protected String channelBankId;

    /** 网关银行ID */
    protected String bankId;

    /** 状态 1.正常 2.失效 */
    protected int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getChannelBankId() {
        return channelBankId;
    }

    public void setChannelBankId(String channelBankId) {
        this.channelBankId = channelBankId;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this,
                                                  ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
