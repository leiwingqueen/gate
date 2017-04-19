package com.elend.gate.channel.facade.vo;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.channel.constant.BankIdConstant;
import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.pay.agent.sdk.constant.PayAgentChannel;

/**
 * 代扣获取验证码参数
 * @author mgt
 *
 */
public class PartnerPayAgentGetCodeData implements Cloneable {
    /**
     * 接入方ID
     */
    private PartnerConstant partnerId;
    /**
     * 接入方订单号
     */
    private String partnerOrderId;
    /**
     * 提现金额
     */
    private BigDecimal amount;
    /**
     * 支付渠道
     */
    private PayAgentChannel channel;
    /**
     * 银行ID
     */
    private BankIdConstant bankId;
    
    /**
     * 银行卡号
     */
    private String cardNo;
    
    /**
     * 用户名
     */
    private String realName;
    
    /**
     * 请求的ip地址
     */
    private String ip;
    
    /**
     * 身份证号码
     */
    private String idNo;
    
    /**
     * 手机号码
     */
    private String cellphone;

    public PartnerConstant getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(PartnerConstant partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerOrderId() {
        return partnerOrderId;
    }

    public void setPartnerOrderId(String partnerOrderId) {
        this.partnerOrderId = partnerOrderId;
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

    public BankIdConstant getBankId() {
        return bankId;
    }

    public void setBankId(BankIdConstant bankId) {
        this.bankId = bankId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
