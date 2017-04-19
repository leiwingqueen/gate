package com.elend.gate.channel.service.vo.yeepay;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.annotation.NodeAnnotation;
import com.elend.gate.annotation.NodeType;

/**
 * 易宝单笔打款返回报文
 * @author mgt
 *
 */
@NodeAnnotation(name="data", type=NodeType.CLASS)
public class YeepaySingleWithdrawRspVO {
    /**命令 */
    @NodeAnnotation(name="cmd")
    public String cmd;
    
    /**返回代码*/
    @NodeAnnotation(name="ret_Code")
    public String retCode;
    
    /**订单号*/
    @NodeAnnotation(name="order_Id")
    public String orderId;

    /**打款状态码*/
    @NodeAnnotation(name="r1_Code")
    public String r1Code;

    /**银行状态*/
    @NodeAnnotation(name="bank_Status")
    public String bankStatus;

    /**错误描述信息*/
    @NodeAnnotation(name="error_Msg")
    public String errMsg;

    /**签名信息*/
    @NodeAnnotation(name="hmac")
    public String hmac;
    public String getCmd() {
        return cmd;
    }
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
    public String getRetCode() {
        return retCode;
    }
    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getR1Code() {
        return r1Code;
    }
    public void setR1Code(String r1Code) {
        this.r1Code = r1Code;
    }
    public String getBankStatus() {
        return bankStatus;
    }
    public void setBankStatus(String bankStatus) {
        this.bankStatus = bankStatus;
    }
    public String getErrMsg() {
        return errMsg;
    }
    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
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
