package com.elend.gate.notify.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class NQueuePO implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -59633056416723944L;

    /** 流水号 */
    protected int id;

    /** 执行时间 */
    protected Date executeTime;

    /** 创建时间 */
    protected Date createTime;

    /** 最后修改时间 */
    protected Date lastModify;

    /** 请求参数 */
    protected String params;

    /** 通知url */
    protected String notifyUrl;

    /** 平台订单号 */
    protected String orderId;

    /** 接入方ID */
    protected String partnerId;

    /** 接入方订单号 */
    protected String partnerOrderId;
    /**
     * 队列ID
     */
    protected int queueIndex;
    /**
     * 发送次数
     */
    protected int retryTimes;
    

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTime) {
        this.retryTimes = retryTime;
    }

    public int getQueueIndex() {
        return queueIndex;
    }

    public void setQueueIndex(int queueIndex) {
        this.queueIndex = queueIndex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastModify() {
        return lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
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

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this,
                                                  ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
