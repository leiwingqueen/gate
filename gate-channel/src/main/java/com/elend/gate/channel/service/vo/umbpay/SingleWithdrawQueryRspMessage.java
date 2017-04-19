package com.elend.gate.channel.service.vo.umbpay;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.annotation.NodeAnnotation;
import com.elend.gate.annotation.NodeType;

/**
 * 宝易互通单笔打款查询返回报文
 * @author mgt
 *
 */
@NodeAnnotation(name="message", type=NodeType.CLASS)
public class SingleWithdrawQueryRspMessage {
    /**报文头*/
    @NodeAnnotation(type=NodeType.CLASS_FIELD)
    public Header header;
    /**报文体*/
    @NodeAnnotation(type=NodeType.CLASS_FIELD)
    public SingleWithdrawQueryRspBody body;

    public SingleWithdrawQueryRspMessage() {
        super();
    }

    public SingleWithdrawQueryRspMessage(Header header,
            SingleWithdrawQueryRspBody body) {
        super();
        this.header = header;
        this.body = body;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public SingleWithdrawQueryRspBody getBody() {
        return body;
    }

    public void setBody(SingleWithdrawQueryRspBody body) {
        this.body = body;
    }

    @Override 
    public String toString() { 
        return ReflectionToStringBuilder.toString(this,ToStringStyle.SHORT_PREFIX_STYLE); 
    }
}
