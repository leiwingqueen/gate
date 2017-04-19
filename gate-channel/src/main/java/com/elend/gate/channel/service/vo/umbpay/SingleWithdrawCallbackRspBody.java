package com.elend.gate.channel.service.vo.umbpay;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.annotation.NodeAnnotation;
import com.elend.gate.annotation.NodeType;

/**
 * 宝易互通单笔打款主动通知请求报文体
 * @author mgt
 *
 */
@NodeAnnotation(name="body", type=NodeType.CLASS)
public class SingleWithdrawCallbackRspBody {
    /**原交易流水号*/
    @NodeAnnotation(name="orgTranFlow")
    public String orgTranFlow;
    
    /**账号*/
    @NodeAnnotation(name="accountNo")
    public String accountNo;
    
    /**账户名称*/
    @NodeAnnotation(name="accountName")
    public String accountName;

    /**交易金额*/
    @NodeAnnotation(name="tranAmt")
    public String tranAmt;
    
    /**交易响应码*/
    @NodeAnnotation(name="tranRespCode")
    public String tranRespCode;

    /**
     * 交 易 返 回 信息描述
     * */
    @NodeAnnotation(name="tranRespMsg")
    public String tranRespMsg;

    /**
     * 交易状态
     * */
    @NodeAnnotation(name="tranState")
    public String tranState;

    public String getOrgTranFlow() {
        return orgTranFlow;
    }

    public void setOrgTranFlow(String orgTranFlow) {
        this.orgTranFlow = orgTranFlow;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getTranAmt() {
        return tranAmt;
    }

    public void setTranAmt(String tranAmt) {
        this.tranAmt = tranAmt;
    }

    public String getTranRespCode() {
        return tranRespCode;
    }

    public void setTranRespCode(String tranRespCode) {
        this.tranRespCode = tranRespCode;
    }

    public String getTranRespMsg() {
        return tranRespMsg;
    }

    public void setTranRespMsg(String tranRespMsg) {
        this.tranRespMsg = tranRespMsg;
    }

    public String getTranState() {
        return tranState;
    }

    public void setTranState(String tranState) {
        this.tranState = tranState;
    }

    @Override 
    public String toString() { 
        return ReflectionToStringBuilder.toString(this,ToStringStyle.SHORT_PREFIX_STYLE); 
    }
}
