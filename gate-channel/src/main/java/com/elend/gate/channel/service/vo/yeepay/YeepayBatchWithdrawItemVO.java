package com.elend.gate.channel.service.vo.yeepay;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.annotation.NodeAnnotation;
import com.elend.gate.annotation.NodeType;

/**
 * 易宝批量打款每条打款请求信息
 * @author mgt
 *
 */
@NodeAnnotation(name="item", type=NodeType.CLASS)
public class YeepayBatchWithdrawItemVO {
    /**订单号 */
    @NodeAnnotation(name="order_Id")
    public String orderId;
    
    /**收款银行编号*/
    @NodeAnnotation(name="bank_Code")
    public String bankCode;
    
    /**联行号*/
    @NodeAnnotation(name="cnaps")
    public String cnaps;

    /**实收款银行全称*/
    @NodeAnnotation(name="bank_Name")
    public String bankName;

    /**收款银行支行名称*/
    @NodeAnnotation(name="branch_Bank_Name")
    public String branchBankName;

    /**打款金额*/
    @NodeAnnotation(name="amount")
    public String amount;
    
    /**帐户名称*/
    @NodeAnnotation(name="account_Name")
    public String accountName;
    
    /**帐户号*/
    @NodeAnnotation(name="account_Number")
    public String accountNumber;

    /**账户类型*/
    @NodeAnnotation(name="account_Type")
    public String accountType;

    /**
     * 收款行省份编码
     * */
    @NodeAnnotation(name="province")
    public String province;

    /**收款行城市编码*/
    @NodeAnnotation(name="city")
    public String city;

    /**
     * 手续费收取方式
     * */
    @NodeAnnotation(name="fee_Type")
    public String feeType;

    /**收款人邮箱*/
    @NodeAnnotation(name="payee_Email")
    public String payeeEmail;

    /**加急*/
    @NodeAnnotation(name="urgency")
    public String urgency;

    /**payee_Mobile*/
    @NodeAnnotation(name="payee_Mobile")
    public String payeeMobile;

    /**留言*/
    @NodeAnnotation(name="leave_Word")
    public String leaveWord;

    /**摘要*/
    @NodeAnnotation(name="abstractInfo")
    public String abstractInfo;

    /**备注*/
    @NodeAnnotation(name="remarksInfo")
    public String remarksInfo;
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public String getCnaps() {
        return cnaps;
    }
    public void setCnaps(String cnaps) {
        this.cnaps = cnaps;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getBranchBankName() {
        return branchBankName;
    }
    public void setBranchBankName(String branchBankName) {
        this.branchBankName = branchBankName;
    }
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getAccountName() {
        return accountName;
    }
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public String getAccountType() {
        return accountType;
    }
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getFeeType() {
        return feeType;
    }
    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }
    public String getPayeeEmail() {
        return payeeEmail;
    }
    public void setPayeeEmail(String payeeEmail) {
        this.payeeEmail = payeeEmail;
    }
    public String getUrgency() {
        return urgency;
    }
    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }
    public String getPayeeMobile() {
        return payeeMobile;
    }
    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }
    public String getLeaveWord() {
        return leaveWord;
    }
    public void setLeaveWord(String leaveWord) {
        this.leaveWord = leaveWord;
    }
    public String getAbstractInfo() {
        return abstractInfo;
    }
    public void setAbstractInfo(String abstractInfo) {
        this.abstractInfo = abstractInfo;
    }
    public String getRemarksInfo() {
        return remarksInfo;
    }
    public void setRemarksInfo(String remarksInfo) {
        this.remarksInfo = remarksInfo;
    }
    @Override 
    public String toString() { 
        return ReflectionToStringBuilder.toString(this,ToStringStyle.SHORT_PREFIX_STYLE); 
    }
}
