package com.elend.gate.channel.vo;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.channel.constant.BankIdConstant;
import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.constant.WithdrawChannel;
import com.elend.gate.channel.model.CBankIdConfigPO;

public class CBankIdConfigVO extends CBankIdConfigPO implements Serializable {

    private static final long serialVersionUID = 3172948880560497350L;
    
    /**
     * 银行名称
     */
    private String bankName;
    
    /**
     * 渠道名称
     */
    private String channelName;
    
    public CBankIdConfigVO() {
        super();
    }
    
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public CBankIdConfigVO(CBankIdConfigPO po) {
        if(po != null) {
            this.id = po.getId();
            this.payChannel = po.getPayChannel();
            this.channelBankId = po.getChannelBankId();
            this.bankId = po.getBankId();
            this.status = po.getStatus();
            
            if(StringUtils.isNotBlank(bankId)) {
                BankIdConstant bank = BankIdConstant.from(bankId);
                this.bankName = bank.getDesc();
            }
            
            if(StringUtils.isNotBlank(payChannel)) {
                try {
                    ChannelIdConstant channel = ChannelIdConstant.from(payChannel);
                    this.channelName = channel.getDesc();
                } catch (IllegalArgumentException e) {
                }
                
                try {
                    WithdrawChannel channel = WithdrawChannel.from(payChannel);
                    this.channelName = channel.getDesc();
                } catch (IllegalArgumentException e) {
                }
            }
        }
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
