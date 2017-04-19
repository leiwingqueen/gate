package com.elend.gate.order.vo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.order.model.PPayAgentAuthRequestPO;

public class PPayAgentAuthRequestVO extends PPayAgentAuthRequestPO {

    private static final long serialVersionUID = 1L;
    
    public PPayAgentAuthRequestVO(PPayAgentAuthRequestPO po) {
        this.id = po.getId();
        this.orderId = po.getOrderId();
        this.partnerId = po.getPartnerId();
        this.partnerOrderId = po.getPartnerOrderId();
        this.amount = po.getAmount();
        this.applyOrderId = po.getApplyOrderId();
        this.channel = po.getChannel();
        this.requestUrl = po.getRequestUrl();
        this.param = po.getParam();
        this.result = po.getResult();
        this.callbackTime = po.getCallbackTime();
        this.callbackResult = po.getCallbackResult();
        this.ip = po.getIp();
        this.status = po.getStatus();
        this.notifyUrl = po.getNotifyUrl();
        this.createTime = po.getCreateTime();
        this.updateTime = po.getUpdateTime();
        this.nextQueryTime = po.getNextQueryTime();
        this.queryNum = po.getQueryNum();
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this,
                                                  ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
