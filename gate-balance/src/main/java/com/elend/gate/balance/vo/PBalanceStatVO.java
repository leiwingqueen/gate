package com.elend.gate.balance.vo;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.balance.constant.ChannelIdConstant;
import com.elend.gate.balance.constant.WithdrawChannel;

public class PBalanceStatVO {
    
    /**  */
    protected long userId;
    
    /** 账本类型：rmb，或者其他货币 */
    protected int balanceType;
    
    /** 当前可用余额 */
    protected BigDecimal usableBalance;
    
    /** 创建时间 */
    protected Date createTime;
    
    /** 最后充值时间 */
    protected Date depositTime;
    
    /** 状态:1 正常，2 可用余额锁定，不能扣款 */
    protected int status;
    
    /** 账本备注 */
    protected String remark;
    
    /** 1:总账,2:渠道分账,3:银行卡 */
    protected int acountType;
    
    /** 收入 */
    protected BigDecimal income;
    
    /**
     * 支出
     */
    protected BigDecimal outlay;
    
    /**
     * 充值渠道
     */
    protected ChannelIdConstant payChannel;
    
    /**
     * 提现渠道
     */
    protected WithdrawChannel withdrawChannel;

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

    public BigDecimal getUsableBalance() {
        return usableBalance;
    }

    public void setUsableBalance(BigDecimal usableBalance) {
        this.usableBalance = usableBalance;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDepositTime() {
        return depositTime;
    }

    public void setDepositTime(Date depositTime) {
        this.depositTime = depositTime;
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

    public int getAcountType() {
        return acountType;
    }

    public void setAcountType(int acountType) {
        this.acountType = acountType;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getOutlay() {
        return outlay;
    }

    public void setOutlay(BigDecimal outlay) {
        this.outlay = outlay;
    }

    public ChannelIdConstant getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(ChannelIdConstant payChannel) {
        this.payChannel = payChannel;
    }

    public WithdrawChannel getWithdrawChannel() {
        return withdrawChannel;
    }

    public void setWithdrawChannel(WithdrawChannel withdrawChannel) {
        this.withdrawChannel = withdrawChannel;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this,
                                                  ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
