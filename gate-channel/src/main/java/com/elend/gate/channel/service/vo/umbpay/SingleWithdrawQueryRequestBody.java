package com.elend.gate.channel.service.vo.umbpay;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.annotation.NodeAnnotation;
import com.elend.gate.annotation.NodeType;

/**
 * 宝易互通单笔打款查询请求报文体
 * @author mgt
 *
 */
@NodeAnnotation(name="body", type=NodeType.CLASS)
public class SingleWithdrawQueryRequestBody {
    /**原交易流水*/
    @NodeAnnotation(name="orgTranFlow")
    public String orgTranFlow;

    public String getOrgTranFlow() {
        return orgTranFlow;
    }

    public void setOrgTranFlow(String orgTranFlow) {
        this.orgTranFlow = orgTranFlow;
    }

    @Override 
    public String toString() { 
        return ReflectionToStringBuilder.toString(this,ToStringStyle.SHORT_PREFIX_STYLE); 
    }
}
