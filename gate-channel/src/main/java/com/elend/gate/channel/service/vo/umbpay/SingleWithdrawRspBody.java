package com.elend.gate.channel.service.vo.umbpay;



import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.annotation.NodeAnnotation;
import com.elend.gate.annotation.NodeType;

/**
 * 保易互通单笔打款返回豹纹体
 * @author mgt
 *
 */
@NodeAnnotation(name="body", type=NodeType.CLASS)
public class SingleWithdrawRspBody {
    /**交易状态*/
    @NodeAnnotation(name="tranState")
    public String tranState;
    
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
