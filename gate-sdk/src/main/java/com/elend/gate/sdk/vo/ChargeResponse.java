package com.elend.gate.sdk.vo;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.sdk.ChannelIdConstant;

/**
 * 参数解析后的参数
 * @author liyongquan 2015年6月8日
 *
 */
public class ChargeResponse {
    /**成功*/
    public static final String SUCCESS_CODE="000";
    /**失败*/
    public static final String FAILURE_CODE = "500";
    /**订单处理中*/
    public static final String ORDER_HANDLING_CODE = "201";
    /**
     * 接入方ID
     */
    private String partnerId;
    /**
     * 返回码
     */
    private String resultCode;
    /**
     * 返回信息
     */
    private String message;
    /**
     * 接入方订单号
     */
    private String partnerOrderId;
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 支付渠道
     */
    private ChannelIdConstant channelId;
    /**
     * 支付渠道订单号
     */
    private String channelOrderId;
    /**
     * 银行ID
     */
    private String bankId;
    /**
     * 支付时间
     */
    private Date channelPayTime;
    /**
     * 支付渠道通知时间
     */
    private Date channelNoticeTime;
    /**
     * 网关通知时间
     */
    private Date gateNoticeTime;
    /**
     * 是否点对点通知
     */
    private boolean isNotify=false;
    /**
     * 合同号
     */
    private String contractNo;
    /**
     * 手续费
     */
    private BigDecimal fee;
    
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this,
                                                  ToStringStyle.SHORT_PREFIX_STYLE);
    }
    public String getPartnerId() {
        return partnerId;
    }
    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }
    public String getResultCode() {
        return resultCode;
    }
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
    public String getPartnerOrderId() {
        return partnerOrderId;
    }
    public void setPartnerOrderId(String partnerOrderId) {
        this.partnerOrderId = partnerOrderId;
    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public ChannelIdConstant getChannelId() {
        return channelId;
    }
    public void setChannelId(ChannelIdConstant channelId) {
        this.channelId = channelId;
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
    public Date getChannelPayTime() {
        return channelPayTime;
    }
    public void setChannelPayTime(Date channelPayTime) {
        this.channelPayTime = channelPayTime;
    }
    public Date getChannelNoticeTime() {
        return channelNoticeTime;
    }
    public void setChannelNoticeTime(Date channelNoticeTime) {
        this.channelNoticeTime = channelNoticeTime;
    }
    public Date getGateNoticeTime() {
        return gateNoticeTime;
    }
    public void setGateNoticeTime(Date gateNoticeTime) {
        this.gateNoticeTime = gateNoticeTime;
    }
    public boolean isNotify() {
        return isNotify;
    }
    public void setNotify(boolean isNotify) {
        this.isNotify = isNotify;
    }
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public BigDecimal getFee() {
        return fee;
    }
    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }
}
