package com.elend.gate.order.model;

import java.io.Serializable;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 订单状态
 * @author mgt
 *
 */
public class POrderStatusRecrodPO implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 7261044440188832884L;

    /** 自增id */
    protected int id;

    /** 平台订单号 */
    protected String orderId;

    /** 交易类型:1充值,2还款,3通标投资,4vip扣费,5提现 */
    protected int orderType;
    
    /** 创建时间 */
    protected Date createTime;
    
    /** 最后修改时间 */
    protected Date lastModify;
    
    /** 渠道（易宝） */
    protected String channel;

    /** 接入方平台 */
    protected String partnerId;

    /** 接入方订单号 */
    protected String partnerOrderId;

    /** 订单的状态 1-网关已接收， 2-第三方请求中, 3-成功, 4-失败 */
    protected int status;

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

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
