package com.elend.gate.order.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.elend.gate.balance.constant.SubSubject;
import com.elend.gate.balance.constant.Subject;
import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.constant.PayType;
import com.elend.gate.channel.constant.WithdrawChannel;
import com.elend.gate.channel.facade.vo.ChargeCallbackData;
import com.elend.gate.channel.facade.vo.PartnerPayAgentChargeData;
import com.elend.gate.channel.facade.vo.PartnerPayAgentCheckCodeData;
import com.elend.gate.channel.facade.vo.PartnerPayAgentGetCodeData;
import com.elend.gate.channel.facade.vo.PartnerWithdrawData;
import com.elend.gate.channel.facade.vo.WithdrawCallbackData;
import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.gate.order.constant.NotifyStatus;
import com.elend.gate.order.constant.RequestSource;
import com.elend.gate.order.facade.vo.DepositRequest;
import com.elend.gate.order.model.POrderPO;
import com.elend.gate.order.model.PWithdrawRequestPO;
import com.elend.gate.order.vo.PPayAgentApplyRequestVO;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.Result;
import com.elend.p2p.ServiceException;
import com.elend.pay.agent.sdk.constant.PayAgentChannel;
import com.elend.pay.agent.sdk.vo.UmbpayAccreditVO;
import com.elend.pay.agent.sdk.vo.UmbpayAgentVO;
import com.elend.pay.agent.sdk.vo.UmbpaySignWhiteListOneVO;

/**
 * 订单处理
 * @author liyongquan 2015年5月28日
 *
 */
public interface OrderService {
    /**
     * 发送充值请求
     *  写入请求表
     * @param channelId--渠道ID
     * @param partnerId--接入方ID
     * @param partnerOrderId--接入方的订单号
     * @param amount--充值金额
     * @param form--请求的表单
     * @param ip--访问IP
     * @param source--请求的来源 网站、手机等
     * @param callbackUrl--回调url
     * @param notifyUrl--点对点通知url
     * @param orderId--支付网关的订单号
     * @return orderId--支付网关的订单号
     */
    Result<String> sendChargeRequest(ChannelIdConstant channelId,PartnerConstant partnerId,
                                     String partnerOrderId,BigDecimal amount,
                                     RequestFormData form,String ip,RequestSource source,
                                     String callbackUrl,String notifyUrl,String orderId, PayType payType);
    /**
     * 充值回调
     * @param resultCode--返回代码
     * @param channelId--渠道ID
     * @param callbackData--回调的数据
     * @param params--支付网关回调参数(解析前)
     * @throws ServiceException--失败回滚
     * @return orderId--支付网关的订单号
     */
    Result<String> chargeCallback(String resultCode,ChannelIdConstant channelId,
                                  ChargeCallbackData callbackData,Map<String,String> params)throws ServiceException;
    
    /**
     * 获取充值请求
     * @param orderId--网关订单号
     * @return
     */
    DepositRequest getChargeRequest(String orderId);
    /**
     * 更新通知状态
     * @param orderId--订单号
     * @param status--通知状态
     * @return
     */
    Result<String> updateNotifyStatus(String orderId,NotifyStatus status);
    
    /**
     * 
     * @param partnerResult
     * @param orderId
     * @return
     */
    Result<String> sendWithdrawRequest(PartnerWithdrawData data, String orderId);
    
    /**
     * 提现回调订单处理
     * @param success
     * @param channel
     * @param backData
     * @return
     */
    Result<String> withdrawCallback(boolean success,
                                    WithdrawChannel channel, WithdrawCallbackData backData);
    
    /**
     * 获取提现请求数据
     * @param orderId  网关订单号
     * @return
     */
    PWithdrawRequestPO getWithdrawRequest(String orderId);
    /**
     * 获取订单记录
     * @param partnerId
     * @param partnerOrderId
     * @return
     */
    POrderPO getOrderBypartnerOrderId(PartnerConstant partnerId,
            String partnerOrderId);
    
    /**
     * 转账
     * @param sourceUserId      转出账号
     * @param targetUserId      转入账号
     * @param amount            金额
     * @return                  订单号
     */
    Result<String> transfer(long sourceUserId, long targetUserId,
            BigDecimal amount, Subject subject, SubSubject subSubject, String remark);
    
    /**
     * 
     * @param startTime
     * @return
     */
    void repairBalanceAndLog(Date startTime);
    
    /**
     * 调账
     * @param userId
     * @param amount
     * @return
     */
    Result<String> adjust(long userId, BigDecimal amount);
    
    /**
     * 代扣请求记录
     * @param data
     * @param orderId
     * @return
     */
    Result<String> sendPayAgentGetCodeRequest(PartnerPayAgentGetCodeData data,
            String orderId);
    
    /**
     * 代扣鉴权获取验证码回调订单处理
     * @param success
     * @param channel
     * @param umbpayAccreditVO
     * @return
     */
    Result<String> PayAgentGetCodeCallback(boolean success, String orderId,
            PayAgentChannel channel, UmbpayAccreditVO umbpayAccreditVO);
    
    /**
     * 代扣鉴权校验验证码回调订单处理
     * @param data
     * @return
     */
    Result<PPayAgentApplyRequestVO> sendPayAgentCheckCodeRequest(String orderId,
            PartnerPayAgentCheckCodeData data);
    
    /**
     * 代扣校验验证码回调处理
     * @param success
     * @param orderId
     * @param backData
     * @return
     */
    Result<String> PayAgentCheckCodeCallback(boolean success, PayAgentChannel channel, String orderId,
            UmbpaySignWhiteListOneVO backData);
    
    /**
     * 待收请求记录
     * @param data
     * @return
     */
    Result<String> sendPayAgentChargeRequest(String orderId,
            PartnerPayAgentChargeData data);
    
    /**
     * 代扣回调
     * @param success
     * @param channel
     * @param orderId
     * @param umbpayAgentVO
     * @return
     */
    Result<String> PayAgentChargeCallback(boolean success,
            PayAgentChannel channel, String orderId,
            UmbpayAgentVO umbpayAgentVO);
}
