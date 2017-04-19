package com.elend.gate.channel.vo;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.p2p.util.vo.BaseSearchVO;

public class CBankIdConfigSearchVO extends BaseSearchVO {

    /** 支付渠道 */
    private String payChannel;

    /** 渠道的银行ID */
    private String channelBankId;

    /** 银行ID */
    private String bankId;

    /**
     * 状态
     */
    private int status;
    
    /**
     * 渠道列表
     */
    private List<String> channelList;

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

    public List<String> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<String> channelList) {
        this.channelList = channelList;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
