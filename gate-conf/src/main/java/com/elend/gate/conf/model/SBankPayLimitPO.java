package com.elend.gate.conf.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SBankPayLimitPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 自增id */
    protected int id;

    /** 银行ID */
    protected String bankId;

    /** 支付渠道 */
    protected String channel;

    /** 用户类型, U盾... */
    protected String userType;

    /** 单笔限额 */
    protected BigDecimal singleLimit;

    /** 单笔限额类型，1.正常 2:无限制,3:自行设置,4:不支持 */
    protected int singleLimitType;

    /** 单日限额 */
    protected BigDecimal dayLimit;

    /** 单日限额类型，1.正常 2:无限制,3:自行设置,4:不支持 */
    protected int dayLimitType;

    /** 单月限额 */
    protected BigDecimal monthLimit;

    /** 单月限额类型，1.正常 2:无限制,3:自行设置,4:不支持 */
    protected int monthLimitType;

    /** 备注 */
    protected String remark;

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

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public BigDecimal getSingleLimit() {
        return singleLimit;
    }

    public void setSingleLimit(BigDecimal singleLimit) {
        this.singleLimit = singleLimit;
    }

    public int getSingleLimitType() {
        return singleLimitType;
    }

    public void setSingleLimitType(int singleLimitType) {
        this.singleLimitType = singleLimitType;
    }

    public BigDecimal getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(BigDecimal dayLimit) {
        this.dayLimit = dayLimit;
    }

    public int getDayLimitType() {
        return dayLimitType;
    }

    public void setDayLimitType(int dayLimitType) {
        this.dayLimitType = dayLimitType;
    }

    public BigDecimal getMonthLimit() {
        return monthLimit;
    }

    public void setMonthLimit(BigDecimal monthLimit) {
        this.monthLimit = monthLimit;
    }

    public int getMonthLimitType() {
        return monthLimitType;
    }

    public void setMonthLimitType(int monthLimitType) {
        this.monthLimitType = monthLimitType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        return ReflectionToStringBuilder.toString(this,
                                                  ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
