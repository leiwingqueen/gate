package com.elend.gate.notify.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 提现队列
 * @author mgt
 *
 */
public class NWithdrawQueuePO implements Serializable {
    
    private static final long serialVersionUID = 2455657212215097654L;
    
    /** 流水号 */
    protected int id;
    
    /** 订单号 */
    protected String orderId;

    /** 执行时间 */
    protected Date executeTime;

    /** 创建时间 */
    protected Date createTime;

    /** 最后修改时间 */
    protected Date lastModify;

    /** 提现参数json数据 */
    protected String params;
    
    /**渠道*/
    protected String channel;

    /** 接入方ID */
    protected String partnerId;

    /** 接入方订单号 */
    protected String partnerOrderId;

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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
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
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "NWithdrawQueuePO [id=" + id + ", orderId=" + orderId
                + ", executeTime=" + executeTime + ", createTime="
                + createTime + ", lastModify=" + lastModify + ", channel="
                + channel + ", partnerId=" + partnerId + ", partnerOrderId="
                + partnerOrderId + "]";
    }
}
