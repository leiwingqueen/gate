package com.elend.gate.channel.service.vo.yeepay;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.annotation.NodeAnnotation;
import com.elend.gate.annotation.NodeType;

/**
 * 易宝打款回调响应报文
 * @author mgt
 *
 */
@NodeAnnotation(name="data", type=NodeType.CLASS)
public class YeepayWithdrawCallbackRspVO {
    /**命令 */
    @NodeAnnotation(name="cmd")
    public String cmd = "TransferNotify";
    
    /**客户编号 */
    @NodeAnnotation(name="mer_Id")
    public String merId;
    
    /**打款批次号*/
    @NodeAnnotation(name="batch_No")
    public String batchNo;

    /**订单编号*/
    @NodeAnnotation(name="order_Id")
    public String orderId;

    /**返回码 成功接收通知返回S*/
    @NodeAnnotation(name="ret_Code")
    public String retCode = "S";

    /**签名信息*/
    //cmd 、 mer_Id 、 batch_No 、order_Id 、ret_Code +商户密钥组成字符串
    @NodeAnnotation(name="hmac")
    public String hmac;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    @Override 
    public String toString() { 
        return ReflectionToStringBuilder.toString(this,ToStringStyle.SHORT_PREFIX_STYLE); 
    }
}
