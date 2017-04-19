package com.elend.gate.channel.facade.vo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.channel.annotation.Param;

/**
 * 提现请求参数
 * @author mgt
 *
 */
public class PartnerWithdrawRequest extends PartnerBaseRequest {
    
    /**
     * 金额
     */
    @Param(required=true, sequence=2)
    protected String amount;
    
    /**
     * 渠道
     */
    @Param(required=true, sequence=3)
    protected String channel;
    
    /**
     * 时间戳
     */
    @Param(required=true, sequence=4)
    protected String timeStamp;
    
    /**
     * ip
     */
    @Param(required=true, sequence=5)
    protected String ip;
    
    /**
     * 异步通知地址
     */
    @Param(required=true, sequence=6)
    protected String notifyUrl;
    
    /**
     * 银行ID
     */
    @Param(required=true, sequence=7)
    private String bankId;
    
    /**
     * 银行卡号
     */
    @Param(required=true, sequence=8)
    private String bankAccount;
    
    /**
     * 用户名
     */
    @Param(required=true, sequence=9)
    private String userName;
    
    /**
     * 城市
     */
    @Param(required=true, sequence=10)
    private String bankCityId;
    
    /**
     * 省份
     */
    @Param(required=true, sequence=11)
    private String bankProvinceId;
    
    /**
     * 支行名称
     */
    @Param(required=true, sequence=12)
    private String bankBranchName;
    
    /**
     * 卡类型（1 对公  2 对私）
     */
    @Param(required=true, sequence=13)
    private String accountType;
    
    /**
     * 身份证号,非企业账号允许为空
     */
    @Param(required=false)
    private String identityCard;
   

    public String getBankId() {
        return bankId;
    }



    public void setBankId(String bankId) {
        this.bankId = bankId;
    }



    public String getBankAccount() {
        return bankAccount;
    }



    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }



    public String getUserName() {
        return userName;
    }



    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBankCityId() {
        return bankCityId;
    }



    public void setBankCityId(String bankCityId) {
        this.bankCityId = bankCityId;
    }



    public String getBankProvinceId() {
        return bankProvinceId;
    }



    public void setBankProvinceId(String bankProvinceId) {
        this.bankProvinceId = bankProvinceId;
    }



    public String getBankBranchName() {
        return bankBranchName;
    }



    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }



    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getAmount() {
        return amount;
    }



    public void setAmount(String amount) {
        this.amount = amount;
    }



    public String getChannel() {
        return channel;
    }



    public void setChannel(String channel) {
        this.channel = channel;
    }



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



    public String getIdentityCard() {
        return identityCard;
    }



    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }



    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
