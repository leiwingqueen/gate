package com.elend.gate.order.vo;
import com.elend.gate.order.model.PPayAgentChargeRequestPO;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PPayAgentChargeRequestVO  extends PPayAgentChargeRequestPO{
	public PPayAgentChargeRequestVO(PPayAgentChargeRequestPO po){
		this.id=po.getId();
		this.orderId=po.getOrderId();
		this.partnerId=po.getPartnerId();
		this.partnerOrderId=po.getPartnerOrderId();
		this.channel=po.getChannel();
		this.channelOrderId=po.getChannelOrderId();
		this.requestUrl=po.getRequestUrl();
		this.param=po.getParam();
		this.result=po.getResult();
		this.createTime=po.getCreateTime();
		this.callbackTime=po.getCallbackTime();
		this.callbackResult=po.getCallbackResult();
		this.amount=po.getAmount();
		this.status=po.getStatus();
		this.ip=po.getIp();
		this.notifyUrl=po.getNotifyUrl();
	}
	
	@Override 
    public String toString() { 
            return ReflectionToStringBuilder.toString(this,ToStringStyle.SHORT_PREFIX_STYLE); 
    }
}
