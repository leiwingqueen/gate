package com.elend.gate.balance.vo;

import com.elend.p2p.util.vo.BaseSearchVO;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PUserBalanceLogSearchVO extends BaseSearchVO {

    /** 用户id */
    private long userId;

    /** 账本类型：rmb */
    private int balanceType;

    /** 支付网关交易订单�? */
    private String orderId;

    /** 交易类型(1 入，2�? */
    private int tradeType;

    /** 科目 */
    private int subject;

    /** 子科�? */
    private int subSubject;

    /** 记录状�? */
    private int status;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this,
                                                  ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
