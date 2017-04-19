package com.elend.gate.channel.facade.vo;

import java.util.Date;


/**
 * 认证支付充值相关数据
 * @author liyongquan 2015年7月13日
 *
 */
public class AuthPartnerChargeData extends PartnerChargeData{
    /**
     * 用户ID
     */
    private long userId;
    /**
     * 银行卡号
     */
    private String bankAccount;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 身份证
     */
    private String idCard;
    /**
     * 用户注册时间
     */
    private Date registerTime;
    /**
     * 认证支付协议号
     */
    private String contractNo;
    
    /**
     * 平台绑定手机号
     */
    private String mobilePhone;
    
    public AuthPartnerChargeData(){}
    
    public AuthPartnerChargeData(PartnerChargeData data){
        this.setPartnerId(data.getPartnerId());
        this.setAmount(data.getAmount());
        this.setPartnerOrderId(data.getPartnerOrderId());
        this.setPayChannel(data.getPayChannel());
        this.setRedirectUrl(data.getRedirectUrl());
        this.setNotifyUrl(data.getNotifyUrl());
        this.setBankId(data.getBankId());
        this.setUserId(data.getUserId());
        this.setPayType(data.getPayType());
    }
    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public String getBankAccount() {
        return bankAccount;
    }
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }
    public String getRealName() {
        return realName;
    }
    public void setRealName(String realName) {
        this.realName = realName;
    }
    public String getIdCard() {
        return idCard;
    }
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
}
