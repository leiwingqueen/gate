package com.elend.gate.channel.service.vo.yeepay;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.annotation.NodeAnnotation;
import com.elend.gate.annotation.NodeType;

/**
 * 易宝打款回调报文
 * @author mgt
 *
 */
@NodeAnnotation(name="data", type=NodeType.CLASS)
public class YeepayWithdrawCallbackVO {
    /**命令 */
    @NodeAnnotation(name="cmd")
    public String cmd = "TransferNotify";
    
    /**版本*/
    @NodeAnnotation(name="version")
    public String version = "1.1";
    
    /**总公司商户编号*/
    @NodeAnnotation(name="group_Id")
    public String groupId;

    /**实际发起付款的交易商户编号*/
    @NodeAnnotation(name="mer_Id")
    public String merId;

    /**为空走代付、代发出款*/
    @NodeAnnotation(name="batch_No")
    public String batchNo;

    /**订单编号*/
    @NodeAnnotation(name="order_Id")
    public String orderId;

    /**打款状态*/
    @NodeAnnotation(name="status")
    public String status;

    /**描述*/
    @NodeAnnotation(name="message")
    public String message;

    /**签名信息*/
    //cmd 、 mer_Id 、 batch_No 、order_Id、status、message 的参数值+商户密钥组成字符串
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
