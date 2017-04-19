package com.elend.gate.channel.facade.vo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.channel.annotation.Param;

/**
 * 代扣校验验证码请求参数
 * @author mgt
 *
 */
public class PartnerPayAgentCheckCodeRequest extends PartnerBaseRequest {
    
    /**
     * 时间戳
     */
    @Param(required=true, sequence=2)
    protected String timeStamp;
    
    /**
     * ip
     */
    @Param(required=true, sequence=3)
    protected String ip;
    
    /**
     * 手机验证码
     */
    @Param(required=true, sequence=4)
    protected String authCode;
    
    /**
     * 异步通知地址
     */
    @Param(required=true, sequence=5)
    protected String notifyUrl;
    
    /**
     * P2P代收申请订单号
     */
    @Param(required=true, sequence=6)
    protected String partnerApplyOrderId;

    
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


    public String getAuthCode() {
        return authCode;
    }


    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getPartnerApplyOrderId() {
        return partnerApplyOrderId;
    }

    public void setPartnerApplyOrderId(String partnerApplyOrderId) {
        this.partnerApplyOrderId = partnerApplyOrderId;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
