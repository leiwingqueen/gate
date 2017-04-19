package com.elend.gate.notify.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class NNotifyLogPO implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 6000974021030396924L;

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

    /** 通知状态 1.成功 2.失败 */
    protected int status;

    /** http返回码 */
    protected String httpResultCode;

    /** 返回结果 */
    protected String result;

    /** 队列ID，标识哪个队列发送的 */
    protected int queueIndex;

    /** 平台订单号 */
    protected String orderId;

    /** 接入方ID */
    protected String partnerId;

    /** 接入方订单号 */
    protected String partnerOrderId;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getHttpResultCode() {
        return httpResultCode;
    }

    public void setHttpResultCode(String httpResultCode) {
        this.httpResultCode = httpResultCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getQueueIndex() {
        return queueIndex;
    }

    public void setQueueIndex(int queueIndex) {
        this.queueIndex = queueIndex;
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
