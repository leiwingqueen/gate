package com.elend.gate.channel.facade.vo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.channel.annotation.Param;

/**
 * 代扣请求参数
 * @author mgt
 *
 */
public class PartnerPayAgentChargeRequest extends PartnerBaseRequest {
    
    /**
     * 时间戳
     */
    @Param(required=true, sequence=2)
    protected String timeStamp;
    
    /**
     * ip
     */
    @Param(required=true, sequence=3)
    protected String ip;
    
    /**
     * 银行ID
     */
    @Param(required=true, sequence=4)
    protected String bankId;
    
    /**
     * 银行卡号
     */
    @Param(required=true, sequence=5)
    protected String cardNo;
    
    /**
     * 真实姓名
     */
    @Param(required=true, sequence=6)
    protected String realName;
    
    /**
     * 金额
     */
    @Param(required=true, sequence=7)
    protected String amount;
    
    /**
     * 结果异步通知地址
     */
    @Param(required=true, sequence=8)
    protected String notifyUrl;
    
    /**
     * 渠道
     */
    @Param(required=true, sequence=9)
    protected String channel;
    
    /**
     * 银行卡类型
     */
    @Param(required=true, sequence=10)
    protected String accountType;
    
    /**
     * 身份证号码
     */
    @Param(required=true, sequence=11)
    protected String idNo;
    
    /**
     * 手机号码
     */
    @Param(required=true, sequence=12)
    protected String cellphone;

    
    public String getTimeStamp() {
        return timeStamp;
    }


    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }


    public String getIp() {
        return ip;
    }


    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getBankId() {
        return bankId;
    }


    public void setBankId(String bankId) {
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getChannel() {
        return channel;
    }


    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
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
