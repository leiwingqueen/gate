package com.elend.gate.channel.service.vo.yeepay;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.annotation.NodeAnnotation;
import com.elend.gate.annotation.NodeType;

/**
 * 易宝批量打款每条记录返回信息
 * @author mgt
 *
 */
@NodeAnnotation(name="item", type=NodeType.CLASS)
public class YeepayBatchWithdrawRspItemVO {
    /**订单号 */
    @NodeAnnotation(name="order_Id")
    public String orderId;
    
    /**错误代码*/
    @NodeAnnotation(name="error_Code")
    public String errorCode;
    
    /**错误描述*/
    @NodeAnnotation(name="error_Msg")
    
    public String errorMsg;

    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    
    @Override 
    public String toString() { 
        return ReflectionToStringBuilder.toString(this,ToStringStyle.SHORT_PREFIX_STYLE); 
    }
}
