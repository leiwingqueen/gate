package com.elend.gate.channel.facade.vo;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.channel.constant.BankAccountType;
import com.elend.gate.channel.constant.BankIdConstant;
import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.pay.agent.sdk.constant.PayAgentChannel;

/**
 * 代扣获取验证码参数
 * @author mgt
 *
 */
public class PartnerPayAgentChargeData implements Cloneable {
    /**
     * 接入方ID
     */
    private PartnerConstant partnerId;
    /**
     * 接入方订单号
     */
    private String partnerOrderId;
    
    /**
     * 渠道
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
     * 真实姓名
     */
    private String realName;
    
    /**
     * 金额
     */
    private BigDecimal amount;
    
    /**
     * 结果异步通知地址
     */
    private String notifyUrl;
    
    /**
     * ip
     */
    private String ip;
    
    /**
     * 银行卡类型
     */
    private BankAccountType accountType;
    
    /**
     * 身份证号码
     */
    protected String idNo;
    
    /**
     * 手机号码
     */
    protected String cellphone;


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


    public BigDecimal getAmount() {
        return amount;
    }


    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public String getNotifyUrl() {
        return notifyUrl;
    }


    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }


    public PayAgentChannel getChannel() {
        return channel;
    }


    public void setChannel(PayAgentChannel channel) {
        this.channel = channel;
    }


    public String getIp() {
        return ip;
    }


    public void setIp(String ip) {
        this.ip = ip;
    }

    public BankAccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(BankAccountType accountType) {
        this.accountType = accountType;
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
