package com.elend.gate.channel.service.vo.umbpay;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.annotation.NodeAnnotation;
import com.elend.gate.annotation.NodeType;

/**
 * 宝易互通单笔打款主动通知返回报文
 * @author mgt
 *
 */
@NodeAnnotation(name="message", type=NodeType.CLASS)
public class SingleWithdrawCallbackRspMessage {
    /**报文头*/
    @NodeAnnotation(type=NodeType.CLASS_FIELD)
    public Header header;
    /**报文体*/
    @NodeAnnotation(type=NodeType.CLASS_FIELD)
    public SingleWithdrawCallbackRspBody body;
    
    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public SingleWithdrawCallbackRspBody getBody() {
        return body;
    }

    public void setBody(SingleWithdrawCallbackRspBody body) {
        this.body = body;
    }

    @Override 
    public String toString() { 
        return ReflectionToStringBuilder.toString(this,ToStringStyle.SHORT_PREFIX_STYLE); 
    }
}
