package com.elend.gate.channel.service;

import java.math.BigDecimal;

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
import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.Result;
import com.elend.p2p.ServiceException;
import com.elend.pay.agent.sdk.constant.PayAgentChannel;
import com.elend.pay.agent.sdk.vo.UmbpayAgentVO;
import com.elend.pay.agent.sdk.vo.UmbpaySignWhiteListOneVO;

/**
 * 定义和接入方(p2p-web等)的通讯的参数格式
 * @author liyongquan 2015年6月2日
 *
 */
public interface PartnerService {
    /**
     * 充值传入参数
     * @param params--接入方传入的参数
     * @throws CheckSignatureException--签名验证失败
     * @return 解析后的参数
     */
    PartnerChargeData chargeRequest(PartnerChargeRequest params)throws CheckSignatureException,ServiceException;
    
    /**
     * 生成回调的表单
     * @param channelfId--支付渠道
     * @param partnerId--接入方
     * @param partnerOrderId--接入方订单号
     * @param callbackData--支付渠道返回的结果
     * @param redirectUrl--跳转url
     * @return
     */
    RequestFormData getChargeCallbackForm(ChannelIdConstant channelfId,PartnerConstant partnerId,String partnerOrderId,
            PartnerResult<? extends ChargeCallbackData> callbackData,String redirectUrl);
    
    /**
     * 提现参数校验
     * @param request
     * @return
     */
    PartnerWithdrawData checkWithdrawParams(PartnerWithdrawRequest request)throws ParamException, CheckSignatureException;

    /**
     * 
     * @param channel
     * @param partnerId
     * @param partnerOrderId
     * @param partnerResult
     * @param notifyUrl
     * @return
     */
    RequestFormData getWithdrawCallbackForm(WithdrawChannel channel,
            String partnerId, String partnerOrderId,
            PartnerResult<WithdrawCallbackData> partnerResult,
            String notifyUrl);
    
    /**
     * 签名
     * @param response
     * @return
     */
    String genSignature(PartnerBaseResponse response);

    /**
     * 代扣获取验证码解析参数
     * @param params
     * @return
     */
    PartnerPayAgentGetCodeData payAgentGetCode(
            PartnerPayAgentGetCodeRequest params);

    /**
     * 代扣校验校验码解析参数
     * @param params
     * @return
     */
    PartnerPayAgentCheckCodeData payAgentCheckCode(
            PartnerPayAgentCheckCodeRequest params);

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
    RequestFormData getPayAgentAuthCallbackForm(String channel,
            String orderId, String notifyUrl, BigDecimal authAmt,
            String name, String partnerOrderId,
            Result<UmbpaySignWhiteListOneVO> requestResult);

    /**
     * 待收获取请求参数
     * @param params
     * @return
     */
    PartnerPayAgentChargeData payAgentCharge(
            PartnerPayAgentChargeRequest params);

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
    RequestFormData getPayAgentChargeCallbackForm(PayAgentChannel channel,
            String orderId, String notifyUrl, BigDecimal amount,
            String channelOrderId, String partnerId, String partnerOrderId,
            Result<UmbpayAgentVO> requestResult);
}
