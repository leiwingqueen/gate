package com.elend.gate.order.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.balance.constant.ChannelIdConstant;
import com.elend.gate.order.constant.RequestStatus;
import com.elend.gate.order.model.PDepositRequestPO;

public class PDepositRequestVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5909905596202559073L;

    /** 自增id */
    protected int id;

    /** 订单号 */
    protected String orderId;

    /** 接入方平台 */
    protected String partnerId;

    /** 接入方订单号 */
    protected String partnerTradeNo;

    /** 渠道（易宝） */
    protected ChannelIdConstant payChannel;

    /** 渠道订单号 */
    protected String channelTradeNo;

    /** 请求的url */
    protected String requestUrl;

    /** 请求的参数json格式 */
    protected String paramValue;

    /** 请求同步返回的结果 */
    protected String result;

    /** 创建时间 */
    protected Date createTime;

    /** 回调时间 */
    protected Date callBackTime;

    /** 回调返回的结果json */
    protected String callBackResult;

    /** 金额 */
    protected BigDecimal amount;

    /** 请求状态 */
    protected RequestStatus status;

    /** 请求ip */
    protected String ip;

    /** 请求来源:1web，2:手机.. */
    protected int source;
    /**回调的url*/
    protected String callbackUrl;
    /**点对点通知url*/
    protected String notifyUrl;
    
    /**渠道名称*/
    protected String channelName;
    /**状态描述*/
    protected String statusStr;
    
    /**状态描述*/
    protected int payType;
    
    public PDepositRequestVO() {
        super();
    }
    
    public PDepositRequestVO(PDepositRequestPO po) {
        this.id = po.getId();
        this.orderId = po.getOrderId();
        this.partnerId = po.getPartnerId();
        this.partnerTradeNo = po.getPartnerTradeNo();
        
        try {
            this.payChannel = ChannelIdConstant.from(po.getPayChannel());
            this.channelName = payChannel.getDesc();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        
        this.channelTradeNo = po.getChannelTradeNo();
        this.requestUrl = po.getRequestUrl();
        this.paramValue = po.getParamValue();
        this.result = po.getResult();
        this.createTime = po.getCreateTime();
        this.callBackTime = po.getCallBackTime();
        this.callBackResult = po.getCallBackResult();
        this.amount = po.getAmount();
        
        try {
            this.status = RequestStatus.from((short)po.getStatus());
            this.statusStr = status.getDesc();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        this.ip = po.getIp();
        this.source = po.getSource();
        this.callbackUrl = po.getCallbackUrl();
        this.notifyUrl = po.getNotifyUrl();
        this.payType = po.getPayType();
        
    }

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

    public String getPartnerTradeNo() {
        return partnerTradeNo;
    }

    public void setPartnerTradeNo(String partnerTradeNo) {
        this.partnerTradeNo = partnerTradeNo;
    }

    public String getChannelTradeNo() {
        return channelTradeNo;
    }

    public void setChannelTradeNo(String channelTradeNo) {
        this.channelTradeNo = channelTradeNo;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCallBackTime() {
        return callBackTime;
    }

    public void setCallBackTime(Date callBackTime) {
        this.callBackTime = callBackTime;
    }

    public String getCallBackResult() {
        return callBackResult;
    }

    public void setCallBackResult(String callBackResult) {
        this.callBackResult = callBackResult;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ChannelIdConstant getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(ChannelIdConstant payChannel) {
        this.payChannel = payChannel;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this,
                                                  ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
