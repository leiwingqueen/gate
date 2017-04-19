package com.elend.gate.order.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PPayAgentApplyRequestPO implements Serializable {
	
	/** 自增id */
	protected int id;
	
	/** 订单号 */
	protected String orderId;
	
	/** 接入方平台（P2P） */
	protected String partnerId;
	
	/** 接入方订单号 */
	protected String partnerOrderId;
	
	/** 渠道（民生） */
	protected String channel;
	
	/** 渠道订单号 */
	protected String channelOrderId;
	
	/** 银行ID */
	protected String bankId;
	
	/** 银行卡号 */
	protected String cardNo;
	
	/** 姓名 */
	protected String realName;
	
	/** 身份证 */
	protected String idNo;
	
	/** 手机号码 */
	protected String cellphone;
	
	/** 鉴权金额 */
	protected BigDecimal amount;
	
	/** 申请返回订单号 */
	protected String merOrderId;
	
	/** 申请返回客户号 */
	protected String custId;
	
	/** 申请返回令牌信息 */
	protected String phoneToken;
	
	/** 请求url */
	protected String requestUrl;
	
	/** 请求参数 */
	protected String param;
	
	/** 返回参数 */
	protected String result;
	
	/** 获取验证码请求ip */
	protected String ip;
	
	/** 请求状态 1-处理中，2-成功，3-失败 */
	protected int status;
	
	/** 创建时间 */
	protected Date createTime;
	
	/** 更新时间 */
	protected Date updateTime;
	
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
	
	public String getBankId() {
		return bankId;
	}
	
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	
	public String getCardNo() {
		return cardNo;
	}
	
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	public String getRealName() {
		return realName;
	}
	
	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	public String getIdNo() {
		return idNo;
	}
	
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	
	public String getCellphone() {
		return cellphone;
	}
	
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public String getMerOrderId() {
		return merOrderId;
	}
	
	public void setMerOrderId(String merOrderId) {
		this.merOrderId = merOrderId;
	}
	
	public String getCustId() {
		return custId;
	}
	
	public void setCustId(String custId) {
		this.custId = custId;
	}
	
	public String getPhoneToken() {
		return phoneToken;
	}
	
	public void setPhoneToken(String phoneToken) {
		this.phoneToken = phoneToken;
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
	
	@Override 
    public String toString() { 
            return ReflectionToStringBuilder.toString(this,ToStringStyle.SHORT_PREFIX_STYLE); 
    }
}
