package com.elend.gate.conf.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.conf.constant.PayLimitType;
import com.elend.gate.conf.model.SBankPayLimitPO;

public class SBankPayLimitVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 自增id */
    protected int id;

    /** 银行ID */
    protected String bankId;

    /** 支付渠道 */
    protected String channel;

    /** 用户类型, U盾... */
    protected String userType;
    
    /** 银行名称 */
    protected String bankName;

    /** 单笔限额 */
    protected BigDecimal singleLimit;

    /** 单笔限额类型，1.正常 2:无限制,3:自行设置,4:不支持 */
    protected int singleLimitType;
    /** 单笔限额类型，1.正常 2:无限制,3:自行设置,4:不支持 */
    protected PayLimitType singleLimitTypeEnum;
    /** 单笔限额类型，1.正常 2:无限制,3:自行设置,4:不支持 */
    protected String singleLimitTypeDesc;

    /** 单日限额 */
    protected BigDecimal dayLimit;

    /** 单日限额类型，1.正常 2:无限制,3:自行设置,4:不支持 */
    protected int dayLimitType;
    /** 单日限额类型，1.正常 2:无限制,3:自行设置,4:不支持 */
    protected PayLimitType dayLimitTypeEnum;
    /** 单日限额类型，1.正常 2:无限制,3:自行设置,4:不支持 */
    protected String dayLimitTypeDesc;

    /** 单月限额 */
    protected BigDecimal monthLimit;

    /** 单月限额类型，1.正常 2:无限制,3:自行设置,4:不支持 */
    protected int monthLimitType;
    /** 单月限额类型，1.正常 2:无限制,3:自行设置,4:不支持 */
    protected PayLimitType monthLimitTypeEnum;;
    /** 单月限额类型，1.正常 2:无限制,3:自行设置,4:不支持 */
    protected String monthLimitTypeDesc;

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

    public BigDecimal getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(BigDecimal dayLimit) {
        this.dayLimit = dayLimit;
    }

    public BigDecimal getMonthLimit() {
        return monthLimit;
    }

    public void setMonthLimit(BigDecimal monthLimit) {
        this.monthLimit = monthLimit;
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

    public String getSingleLimitTypeDesc() {
        return singleLimitTypeDesc;
    }

    public void setSingleLimitTypeDesc(String singleLimitTypeDesc) {
        this.singleLimitTypeDesc = singleLimitTypeDesc;
    }

    public String getDayLimitTypeDesc() {
        return dayLimitTypeDesc;
    }

    public void setDayLimitTypeDesc(String dayLimitTypeDesc) {
        this.dayLimitTypeDesc = dayLimitTypeDesc;
    }

    public String getMonthLimitTypeDesc() {
        return monthLimitTypeDesc;
    }

    public void setMonthLimitTypeDesc(String monthLimitTypeDesc) {
        this.monthLimitTypeDesc = monthLimitTypeDesc;
    }

    public int getSingleLimitType() {
        return singleLimitType;
    }

    public void setSingleLimitType(int singleLimitType) {
        this.singleLimitType = singleLimitType;
    }

    public PayLimitType getSingleLimitTypeEnum() {
        return singleLimitTypeEnum;
    }

    public void setSingleLimitTypeEnum(PayLimitType singleLimitTypeEnum) {
        this.singleLimitTypeEnum = singleLimitTypeEnum;
    }

    public int getDayLimitType() {
        return dayLimitType;
    }

    public void setDayLimitType(int dayLimitType) {
        this.dayLimitType = dayLimitType;
    }

    public PayLimitType getDayLimitTypeEnum() {
        return dayLimitTypeEnum;
    }

    public void setDayLimitTypeEnum(PayLimitType dayLimitTypeEnum) {
        this.dayLimitTypeEnum = dayLimitTypeEnum;
    }

    public int getMonthLimitType() {
        return monthLimitType;
    }

    public void setMonthLimitType(int monthLimitType) {
        this.monthLimitType = monthLimitType;
    }

    public PayLimitType getMonthLimitTypeEnum() {
        return monthLimitTypeEnum;
    }

    public void setMonthLimitTypeEnum(PayLimitType monthLimitTypeEnum) {
        this.monthLimitTypeEnum = monthLimitTypeEnum;
    }

    public SBankPayLimitVO(SBankPayLimitPO po) {
        this.id = po.getId();
        this.bankId = po.getBankId();
        this.channel = po.getChannel();
        this.userType = po.getUserType();
        this.singleLimit = po.getSingleLimit();
        this.singleLimitType = po.getSingleLimitType();
        if(po.getSingleLimitType() > 0) {
            this.singleLimitTypeEnum = PayLimitType.from(po.getSingleLimitType());
            this.singleLimitTypeDesc = singleLimitTypeEnum.getDesc();
        }
        this.dayLimit = po.getDayLimit();
        this.dayLimitType = po.getDayLimitType();
        if(po.getDayLimitType() > 0) {
            this.dayLimitTypeEnum = PayLimitType.from(po.getDayLimitType());
            this.dayLimitTypeDesc = dayLimitTypeEnum.getDesc();
        }
        this.monthLimit = po.getMonthLimit();
        this.monthLimitType = po.getMonthLimitType();
        if(po.getMonthLimitType() > 0) {
            this.monthLimitTypeEnum = PayLimitType.from(po.getMonthLimitType());
            this.monthLimitTypeDesc = monthLimitTypeEnum.getDesc();
        }
        this.remark = po.getRemark();
        this.createTime = po.getCreateTime();
    }

    public SBankPayLimitVO() {
        super();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this,
                                                  ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
