package com.elend.gate.order.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PPayAgentAuthRequestPO implements Serializable {
	
	/** 自增id */
	protected int id;
	
	/** 订单号 */
	protected String orderId;
	
	/** 接入方平台（P2P） */
	protected String partnerId;
	
	/** 接入方订单号 */
	protected String partnerOrderId;
	
	/** 鉴权金额 */
	protected BigDecimal amount;
	
	/** 鉴权申请订单号 */
	protected String applyOrderId;
	
	/** 渠道（民生） */
	protected String channel;
	
	/** 获取验证码请求url */
	protected String requestUrl;
	
	/** 获取验证码的请求参数 */
	protected String param;
	
	/** 获取验证码返回参数 */
	protected String result;
	
	/** 认证回调时间 */
	protected Date callbackTime;
	
	/** 认证回调返回的结果 */
	protected String callbackResult;
	
	/** 认证请求ip */
	protected String ip;
	
	/** 请求状态 1-处理中，2-成功，3-失败 */
	protected int status;
	
	/** 点对点通知的url */
	protected String notifyUrl;
	
	/** 创建时间 */
	protected Date createTime;
	
	/** 更新时间 */
	protected Date updateTime;
	
	/** 下次查询时间 */
	protected Date nextQueryTime;
	
	/** 第三方结果查询次数 */
	protected int queryNum;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
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
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public String getApplyOrderId() {
		return applyOrderId;
	}
	
	public void setApplyOrderId(String applyOrderId) {
		this.applyOrderId = applyOrderId;
	}
	
	public String getChannel() {
		return channel;
	}
	
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	public String getRequestUrl() {
		return requestUrl;
	}
	
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	
	public String getParam() {
		return param;
	}
	
	public void setParam(String param) {
		this.param = param;
	}
	
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public Date getCallbackTime() {
		return callbackTime;
	}
	
	public void setCallbackTime(Date callbackTime) {
		this.callbackTime = callbackTime;
	}
	
	public String getCallbackResult() {
		return callbackResult;
	}
	
	public void setCallbackResult(String callbackResult) {
		this.callbackResult = callbackResult;
	}
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getNotifyUrl() {
		return notifyUrl;
	}
	
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Date getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public Date getNextQueryTime() {
		return nextQueryTime;
	}
	
	public void setNextQueryTime(Date nextQueryTime) {
		this.nextQueryTime = nextQueryTime;
	}
	
	public int getQueryNum() {
		return queryNum;
	}
	
	public void setQueryNum(int queryNum) {
		this.queryNum = queryNum;
	}
	
	@Override 
    public String toString() { 
            return ReflectionToStringBuilder.toString(this,ToStringStyle.SHORT_PREFIX_STYLE); 
    }
}
