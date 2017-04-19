package com.elend.gate.channel.facade.vo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.channel.annotation.Param;

/**
 * 代扣获取验证码请求参数
 * @author mgt
 *
 */
public class PartnerPayAgentGetCodeRequest extends PartnerBaseRequest {
    
    /**
     * 金额
     */
    @Param(required=true, sequence=2)
    protected String amount;
    
    /**
     * 渠道
     */
    @Param(required=true, sequence=3)
    protected String channel;
    
    /**
     * 时间戳
     */
    @Param(required=true, sequence=4)
    protected String timeStamp;
    
    /**
     * ip
     */
    @Param(required=true, sequence=5)
    protected String ip;
    
    /**
     * 银行ID
     */
    @Param(required=true, sequence=6)
    private String bankId;
    
    /**
     * 银行卡号
     */
    @Param(required=true, sequence=7)
    private String cardNo;
    
    /**
     * 姓名
     */
    @Param(required=true, sequence=8)
    private String realName;

    /**
     * 身份证号码
     */
    @Param(required=true, sequence=9)
    private String idNo;
    
    /**
     * 手机号码
     */
    @Param(required=true, sequence=10)
    private String cellphone;

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
