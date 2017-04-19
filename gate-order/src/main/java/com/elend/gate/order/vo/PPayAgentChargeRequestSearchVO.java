package com.elend.gate.order.vo;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.p2p.util.vo.BaseSearchVO;

public class PPayAgentChargeRequestSearchVO extends BaseSearchVO{

	/** 订单号 */
	private String orderId;
	
	/** 接入方平台（P2P） */
	private String partnerId;
	
	/** 接入方订单号 */
	private String partnerOrderId;
	
	/** 渠道（民生） */
	private String channel;
	
	/** 渠道订单号 */
	private String channelOrderId;
	
	/** 创建时间 */
	private Date createTime;
	
	/**
	 * 开始时间
	 */
	private Date startTime;
	
	/**
	 * 结束时间
	 */
	private Date endTime;
	
	/** 请求状态 1申请，2成功，3失败 */
	private int status;
	
	/** 下次查询的时间 */
	private Date nextQueryTime;
	
	public String getOrderId() {
		return orderId;
	}
	
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public String getPartnerId() {
		return partnerId;
	}
	
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	
	public String getPartnerOrderId() {
		return partnerOrderId;
	}
	
	public void setPartnerOrderId(String partnerOrderId) {
		this.partnerOrderId = partnerOrderId;
	}
	
	public String getChannel() {
		return channel;
	}
	
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	public String getChannelOrderId() {
		return channelOrderId;
	}
	
	public void setChannelOrderId(String channelOrderId) {
		this.channelOrderId = channelOrderId;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getNextQueryTime() {
        return nextQueryTime;
    }

    public void setNextQueryTime(Date nextQueryTime) {
        this.nextQueryTime = nextQueryTime;
    }

    @Override 
    public String toString() { 
            return ReflectionToStringBuilder.toString(this,ToStringStyle.SHORT_PREFIX_STYLE); 
    }
}
