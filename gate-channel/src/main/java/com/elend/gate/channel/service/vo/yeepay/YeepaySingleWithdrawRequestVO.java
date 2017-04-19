package com.elend.gate.channel.service.vo.yeepay;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.annotation.NodeAnnotation;
import com.elend.gate.annotation.NodeType;

/**
 * 易宝单笔打款请求报文
 * @author mgt
 *
 */
@NodeAnnotation(name="data", type=NodeType.CLASS)
public class YeepaySingleWithdrawRequestVO {
    /**命令 */
    @NodeAnnotation(name="cmd")
    public String cmd = "TransferSingle";
    
    /**版本*/
    @NodeAnnotation(name="version")
    public String version = "1.1";
    
    /**总公司商户编号*/
    @NodeAnnotation(name="group_Id")
    public String groupId;

    /**实际发起付款的交易商户编号*/
    @NodeAnnotation(name="mer_Id")
    public String merId;

    /**产品类型  为空走代付、代发出款*/
    @NodeAnnotation(name="product")
    public String product;

    /**为空走代付、代发出款*/
    @NodeAnnotation(name="batch_No")
    public String batchNo;

    /**收款银行编号*/
    @NodeAnnotation(name="bank_Code")
    public String bankCode;

    /**订单号*/
    @NodeAnnotation(name="order_Id")
    public String orderId;

    /**联行号*/
    @NodeAnnotation(name="cnaps")
    public String cnaps;

    /**收款银行全称*/
    @NodeAnnotation(name="bank_Name")
    public String bankName;

    /**收款银行支收款银行支*/
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
    public String accoutType;

    /**收款行省份编码*/
    @NodeAnnotation(name="province")
    public String province;

    /**收款行城市编码*/
    @NodeAnnotation(name="city")
    public String city;

    /**手续费收取方式*/
    @NodeAnnotation(name="fee_Type")
    public String feeType;

    /**收款人邮箱*/
    @NodeAnnotation(name="payee_Email")
    public String payeeEmail;

    /**收款人手机号*/
    @NodeAnnotation(name="payee_Mobile")
    public String payeeMobile;

    /**加急*/
    @NodeAnnotation(name="urgency")
    public String urgency;

    /**留言*/
    @NodeAnnotation(name="leave_Word")
    public String leaveWord;

    /**摘要*/
    @NodeAnnotation(name="abstractInfo")
    public String abstractInfo;

    /**备注*/
    @NodeAnnotation(name="remarksInfo")
    public String remarksInfo;

    /**签名信息*/
    @NodeAnnotation(name="hmac")
    public String hmac;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getAccoutType() {
        return accoutType;
    }

    public void setAccoutType(String accoutType) {
        this.accoutType = accoutType;
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

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
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

    public String getHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }
    @Override 
    public String toString() { 
        return ReflectionToStringBuilder.toString(this,ToStringStyle.SHORT_PREFIX_STYLE); 
    }
}
