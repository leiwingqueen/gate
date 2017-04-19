package com.elend.gate.channel.facade.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.elend.gate.channel.constant.ChargeQueryStatus;

/**
 * 充值单笔查询
 * @author mgt
 */
public class PartnerSingleQueryChargeData {
    /**
     * 支付渠道订单号
     */
    private String channelOrderId;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 平台订单号
     */
    private String orderId;

    /**
     * 交易支付时间
     */
    private Date createTime;
    
    /**
     * 订单成功时间
     */
    private Date finishTime;

    /**
     * 订单的状态
     */
    private ChargeQueryStatus status;

	public String getChannelOrderId() {
		return channelOrderId;
	}

	public void setChannelOrderId(String channelOrderId) {
		this.channelOrderId = channelOrderId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public ChargeQueryStatus getStatus() {
		return status;
	}

	public void setStatus(ChargeQueryStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "PartnerSingleQueryChargeData [channelOrderId=" + channelOrderId
				+ ", amount=" + amount + ", orderId=" + orderId
				+ ", createTime=" + createTime + ", finishTime=" + finishTime
				+ ", status=" + status + "]";
	}
}
