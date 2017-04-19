package com.elend.gate.balance.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PUserBalanceLogPO implements Serializable {

    /** 自增id */
    protected int id;

    /** 用户id */
    protected long userId;

    /** 账本类型：rmb */
    protected int balanceType;

    /** 使用金额 */
    protected BigDecimal amount;

    /** 当前余额 */
    protected BigDecimal balance;

    /** 支付网关交易订单�? */
    protected String orderId;

    /** 交易类型(1 入，2�? */
    protected int tradeType;

    /** 科目 */
    protected int subject;

    /** 子科�? */
    protected int subSubject;

    /** 创建时间 */
    protected Date createTime;

    /** 记录状�? */
    protected int status;

    /** 备注 */
    protected String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(int balanceType) {
        this.balanceType = balanceType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public int getSubSubject() {
        return subSubject;
    }

    public void setSubSubject(int subSubject) {
        this.subSubject = subSubject;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this,
                                                  ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
