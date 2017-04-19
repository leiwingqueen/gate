package com.elend.gate.channel.facade.vo;

import java.math.BigDecimal;

import com.elend.gate.channel.constant.BankIdConstant;
import com.elend.gate.channel.constant.WithdrawChannel;
import com.elend.gate.conf.constant.PartnerConstant;

/**
 * 接入方提现的参数
 * @author mgt
 *
 */
public class PartnerWithdrawData implements Cloneable {
    
    /**
     * 对公
     */
    public static final String  ACCOUNT_TYPE_PUBLIC = "1";
    /**
     * 对私
     */
    public static final String  ACCOUNT_TYPE_PRIVATE = "2";
    
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
    private WithdrawChannel channel;
    /**
     * 点对点通知url
     */
    private String notifyUrl;
    /**
     * 银行ID
     */
    private BankIdConstant bankId;
    
    /**
     * 银行卡号
     */
    private String bankAccount;
    
    /**
     * 用户名
     */
    private String userName;
    
    /**
     * 城市
     */
    private String bankCityId;
    
    /**
     * 省份
     */
    private String bankProvinceId;
    
    /**
     * 支行名称
     */
    private String bankBranchName;
    
    /**
     * 卡类型（1 对公  2 对私）
     */
    private String accountType;
    
    /**
     * 请求的ip地址
     */
    private String ip;
    /**
     * 用户身份证号
     */
    private String identityCard;

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

    public WithdrawChannel getChannel() {
        return channel;
    }

    public void setChannel(WithdrawChannel channel) {
        this.channel = channel;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public BankIdConstant getBankId() {
        return bankId;
    }

    public void setBankId(BankIdConstant bankId) {
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
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "PartnerWithdrawData [partnerId=" + partnerId
                + ", partnerOrderId=" + partnerOrderId + ", amount=" + amount
                + ", channel=" + channel + ", notifyUrl=" + notifyUrl
                + ", bankId=" + bankId + ", bankCityId=" + bankCityId
                + ", bankProvinceId=" + bankProvinceId + ", bankBranchName="
                + bankBranchName + ", accountType=" + accountType + ", ip="
                + ip + "]";
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }
}
