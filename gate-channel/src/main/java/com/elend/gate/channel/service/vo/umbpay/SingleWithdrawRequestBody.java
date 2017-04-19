package com.elend.gate.channel.service.vo.umbpay;



import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.annotation.NodeAnnotation;
import com.elend.gate.annotation.NodeType;

/**
 * 宝易互通单笔打款请求报文体
 * @author mgt
 *
 */
@NodeAnnotation(name="body", type=NodeType.CLASS)
public class SingleWithdrawRequestBody {
    /**商户别名*/
    @NodeAnnotation(name="merPlatAcctAlias")
    public String merPlatAcctAlias;
    
    /**协议号*/
    @NodeAnnotation(name="protocolNo")
    public String protocolNo;
    
    /**银行名称*/
    @NodeAnnotation(name="bankName")
    public String bankName;

    /**账号*/
    @NodeAnnotation(name="accountNo")
    public String accountNo;
    
    /**账户名称*/
    @NodeAnnotation(name="accountName")
    public String accountName;

    /**账号类型
     * 00：对私
     * 01：对公
     * */
    @NodeAnnotation(name="accountType")
    public String accountType;

    /**
     * 开户行所在省
     * 不 带 “ 省 ” 或“自治区”
     * */
    @NodeAnnotation(name="openProvince")
    public String openProvince;

    /**开户行所在市*/
    @NodeAnnotation(name="openCity")
    public String openCity;

    /**开户行名称*/
    @NodeAnnotation(name="openName")
    public String openName;

    /**交易金额*/
    @NodeAnnotation(name="tranAmt")
    public String tranAmt;

    /**币种*/
    @NodeAnnotation(name="curType")
    public String curType = "CNY";
    
    /**业务类型*/
    @NodeAnnotation(name="bsnType")
    public String bsnType;

    /**
     * 开户证件类型
     * 01：身份证
     * */
    @NodeAnnotation(name="certType")
    public String certType;

    /**证件号*/
    @NodeAnnotation(name="certNo")
    public String certNo;

    /**手机*/
    @NodeAnnotation(name="mobileNo")
    public String mobileNo;

    /**商品信息*/
    @NodeAnnotation(name="prodInfo")
    public String prodInfo;

    /**附加信息*/
    @NodeAnnotation(name="msgExt")
    public String msgExt;

    public String getMerPlatAcctAlias() {
        return merPlatAcctAlias;
    }

    public void setMerPlatAcctAlias(String merPlatAcctAlias) {
        this.merPlatAcctAlias = merPlatAcctAlias;
    }

    public String getProtocolNo() {
        return protocolNo;
    }

    public void setProtocolNo(String protocolNo) {
        this.protocolNo = protocolNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getOpenProvince() {
        return openProvince;
    }

    public void setOpenProvince(String openProvince) {
        this.openProvince = openProvince;
    }

    public String getOpenCity() {
        return openCity;
    }

    public void setOpenCity(String openCity) {
        this.openCity = openCity;
    }

    public String getOpenName() {
        return openName;
    }

    public void setOpenName(String openName) {
        this.openName = openName;
    }

    public String getTranAmt() {
        return tranAmt;
    }

    public void setTranAmt(String tranAmt) {
        this.tranAmt = tranAmt;
    }

    public String getCurType() {
        return curType;
    }

    public void setCurType(String curType) {
        this.curType = curType;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getProdInfo() {
        return prodInfo;
    }

    public void setProdInfo(String prodInfo) {
        this.prodInfo = prodInfo;
    }

    public String getMsgExt() {
        return msgExt;
    }

    public void setMsgExt(String msgExt) {
        this.msgExt = msgExt;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBsnType() {
        return bsnType;
    }

    public void setBsnType(String bsnType) {
        this.bsnType = bsnType;
    }

    @Override 
    public String toString() { 
        return ReflectionToStringBuilder.toString(this,ToStringStyle.SHORT_PREFIX_STYLE); 
    }
}
