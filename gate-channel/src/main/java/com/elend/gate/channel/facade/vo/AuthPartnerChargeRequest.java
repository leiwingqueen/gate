package com.elend.gate.channel.facade.vo;

import com.elend.gate.channel.annotation.Param;

/**
 * 认证支付参数
 * @author liyongquan 2015年7月13日
 *
 */
public class AuthPartnerChargeRequest extends PartnerChargeRequest{

    /**
     * 银行卡号
     */
    @Param(required=false, sequence=11)
    private String bankAccount;
    /**
     * 真实姓名
     */
    @Param(required=false, sequence=12)
    private String realName;
    /**
     * 身份证
     */
    @Param(required=false, sequence=13)
    private String idCard;
    /**
     * 注册时间
     */
    @Param(required=false, sequence=14)
    private String registerTime;
    
    /**
     * 协议号
     */
    @Param(required=false, sequence=15)
    private String contractNo;
    
    /**
     * 协议号
     */
    @Param(required=false, sequence=16)
    private String mobilePhone;
    
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
    public String getRegisterTime() {
        return registerTime;
    }
    public void setRegisterTime(String registerTime) {
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
