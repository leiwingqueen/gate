package com.elend.gate.channel.service.vo.umbpay;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.annotation.NodeAnnotation;
import com.elend.gate.annotation.NodeType;

/**
 * 宝易互通单笔打款查询返回报文体
 * @author mgt
 *
 */
@NodeAnnotation(name="body", type=NodeType.CLASS)
public class SingleWithdrawQueryRspBody {
    /**交易响应码（打款时一致）*/
    @NodeAnnotation(name="tranRespCode")
    public String tranRespCode;
    /**交易返回信息描述*/
    @NodeAnnotation(name="tranRespMsg")
    public String tranRespMsg;
    /**交易状态*/
    @NodeAnnotation(name="tranState")
    public String tranState;

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
