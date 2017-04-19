package com.elend.gate.channel.facade;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.elend.gate.channel.PartnerResult;
import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.constant.WithdrawChannel;
import com.elend.gate.channel.exception.CheckSignatureException;
import com.elend.gate.channel.exception.ParamException;
import com.elend.gate.channel.facade.vo.ChargeCallbackData;
import com.elend.gate.channel.facade.vo.PartnerBaseResponse;
import com.elend.gate.channel.facade.vo.PartnerChargeData;
import com.elend.gate.channel.facade.vo.PartnerChargeRequest;
import com.elend.gate.channel.facade.vo.PartnerPayAgentChargeData;
import com.elend.gate.channel.facade.vo.PartnerPayAgentChargeRequest;
import com.elend.gate.channel.facade.vo.PartnerPayAgentCheckCodeData;
import com.elend.gate.channel.facade.vo.PartnerPayAgentCheckCodeRequest;
import com.elend.gate.channel.facade.vo.PartnerPayAgentGetCodeData;
import com.elend.gate.channel.facade.vo.PartnerPayAgentGetCodeRequest;
import com.elend.gate.channel.facade.vo.PartnerWithdrawData;
import com.elend.gate.channel.facade.vo.PartnerWithdrawRequest;
import com.elend.gate.channel.facade.vo.WithdrawCallbackData;
import com.elend.gate.channel.service.PartnerService;
import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.Result;
import com.elend.p2p.ServiceException;
import com.elend.pay.agent.sdk.constant.PayAgentChannel;
import com.elend.pay.agent.sdk.vo.UmbpayAgentVO;
import com.elend.pay.agent.sdk.vo.UmbpaySignWhiteListOneVO;

/**
 * 定义和接入方(p2p-web等)的通讯的参数格式
 * @author liyongquan 2015年6月1日
 *
 */
@Component
public class PartnerFacade {
    @Autowired
    @Qualifier("partnerService")
    private PartnerService service;
    
    @Autowired
    @Qualifier("authPartnerService")
    private PartnerService authPartnerService;
    
    /**
     * 充值传入参数
     * @param params--接入方传入的参数
     * @throws CheckSignatureException--签名验证失败
     * @return 解析后的参数
     */
    public PartnerChargeData chargeRequest(PartnerChargeRequest params)throws CheckSignatureException,ServiceException{
        ChannelIdConstant payChannel=ChannelIdConstant.from(params.getPayChannel());
        if(payChannel.isAuthPay()){
            return authPartnerService.chargeRequest(params);
	}
	return service.chargeRequest(params);
    }
    
    /**
     * 生成回调的表单
     * @param channelId--支付渠道
     * @param partnerId--接入方
     * @param partnerOrderId--接入方订单号
     * @param callbackData--支付渠道返回的结果
     * @param redirectUrl--跳转url
     * @return
     */
    public RequestFormData getChargeCallbackForm(ChannelIdConstant channelId,PartnerConstant partnerId,String partnerOrderId,
            PartnerResult<ChargeCallbackData> callbackData,String redirectUrl){
        return service.getChargeCallbackForm(channelId, partnerId, partnerOrderId, callbackData, redirectUrl);
    }

    /**
     * 提现代付传入参数解析
     * @param params--接入方传入的参数
     * @throws CheckSignatureException--签名验证失败
     * @return 解析后的参数
     */
    public PartnerWithdrawData withdrawRequest(PartnerWithdrawRequest request)throws ParamException, CheckSignatureException{
        return service.checkWithdrawParams(request);
    }

    /**
     * 生成提现回调的表单
     * @param channel           渠道
     * @param partnerId         借入方
     * @param partnerOrderId    接入方订单号
     * @param partnerResult     支付渠道结果
     * @param notifyUrl         通知地址
     * @return
     */
    public RequestFormData getWithdrawCallbackForm(WithdrawChannel channel,
            String partnerId, String partnerOrderId,
            PartnerResult<WithdrawCallbackData> partnerResult,
            String notifyUrl) {
        return service.getWithdrawCallbackForm(channel, partnerId, partnerOrderId, partnerResult, notifyUrl);
    }
    
    /**
     * 签名
     * @param response
     * @return
     */
    public String genSignature(PartnerBaseResponse response) {
        return service.genSignature(response);
    }

    /**
     * 代扣获取验证码解析参数
     * @param params
     * @return
     */
    public PartnerPayAgentGetCodeData payAgentGetCode(
            PartnerPayAgentGetCodeRequest params) {
        return service.payAgentGetCode(params);
    }

    /**
     * 代扣校验校验码解析参数
     * @param params
     * @return
     */
    public PartnerPayAgentCheckCodeData payAgentCheckCode(
            PartnerPayAgentCheckCodeRequest params) {
        return service.payAgentCheckCode(params);
    }
    
    /**
     * 待收鉴权回调参数
     * @param channel
     * @param orderId
     * @param notifyUrl
     * @param authAmt
     * @param channelOrderId
     * @param name
     * @param partnerOrderId
     * @param requestResult
     * @return
     */
    public RequestFormData getPayAgentAuthCallbackForm(String channel,
            String orderId, String notifyUrl, BigDecimal authAmt,
            String name, String partnerOrderId,
            Result<UmbpaySignWhiteListOneVO> requestResult) {
        return service.getPayAgentAuthCallbackForm(channel,
                                                   orderId, notifyUrl, authAmt,
                                                   name, partnerOrderId,
                                                   requestResult);
    }

    /**
     * 待收获取请求参数
     * @param params
     * @return
     */
    public PartnerPayAgentChargeData payAgentCharge(
            PartnerPayAgentChargeRequest params) {
        return service.payAgentCharge(params);
    }

    /**
     * 代收回调表单
     * @param channel
     * @param orderId
     * @param notifyUrl
     * @param amount
     * @param bussflowno
     * @param name
     * @param partnerOrderId
     * @param requestResult
     * @return
     */
    public RequestFormData getPayAgentChargeCallbackForm(
            PayAgentChannel channel, String orderId, String notifyUrl,
            BigDecimal amount, String channelOrderId, String partnerId,
            String partnerOrderId, Result<UmbpayAgentVO> requestResult) {
        return service.getPayAgentChargeCallbackForm(channel, orderId, notifyUrl,
                                                     amount, channelOrderId, partnerId,
                                                     partnerOrderId, requestResult);
    }
}
