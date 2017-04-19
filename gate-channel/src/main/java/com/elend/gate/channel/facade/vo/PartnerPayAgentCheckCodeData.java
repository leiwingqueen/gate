package com.elend.gate.channel.facade.vo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.elend.gate.conf.constant.PartnerConstant;

/**
 * 代扣获取验证码参数
 * @author mgt
 *
 */
public class PartnerPayAgentCheckCodeData implements Cloneable {
    /**
     * 接入方ID
     */
    private PartnerConstant partnerId;
    /**
     * 接入方订单号
     */
    private String partnerOrderId;
    /**
     * 提现金额
     */
    private String authCode;
    
    /**
     * 请求的ip地址
     */
    private String ip;
    
    /**
     * 异步通知地址
     */
    private String notifyUrl;
    
    /**
     * P2P代收申请订单号
     */
    private String partnerApplyOrderId;

    
    public PartnerConstant getPartnerId() {
        return partnerId;
    }


    public void setPartnerId(PartnerConstant partnerId) {
        this.partnerId = partnerId;
    }


    public String getPartnerOrderId() {
        return partnerOrderId;
    }


    public void setPartnerOrderId(String partnerOrderId) {
        this.partnerOrderId = partnerOrderId;
    }


    public String getAuthCode() {
        return authCode;
    }


    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }


    public String getIp() {
        return ip;
    }


    public void setIp(String ip) {
        this.ip = ip;
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
