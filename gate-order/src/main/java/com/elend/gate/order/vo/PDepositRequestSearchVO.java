package com.elend.gate.order.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.elend.p2p.util.vo.BaseSearchVO;


/**
 * 充值请求查询条件
 * @author mgt
 */
public class PDepositRequestSearchVO extends BaseSearchVO {
        @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss" )
	private Date startDate;
        @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss" )
	private Date endDate;
	
    /**
     * 渠道
     */
    private String payChannel;

    /**
     * 接入方订单号
     */
    private String partnerTradeNo;

    /**
     * 网关订单号
     */
    private String orderId;

    /**
     * 状态
     */
    private int status;
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
    public String getPayChannel() {
        return payChannel;
    }
    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }
    public String getPartnerTradeNo() {
        return partnerTradeNo;
    }
    public void setPartnerTradeNo(String partnerTradeNo) {
        this.partnerTradeNo = partnerTradeNo;
    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return "PDepositRequestSearchVO [startDate=" + startDate
                + ", endDate=" + endDate + ", payChannel=" + payChannel
                + ", partnerTradeNo=" + partnerTradeNo + ", orderId="
                + orderId + ", status=" + status + "]";
    }
}
