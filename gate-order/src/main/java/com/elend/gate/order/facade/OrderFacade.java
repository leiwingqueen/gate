package com.elend.gate.order.facade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
import com.elend.gate.order.service.OrderService;
import com.elend.gate.order.vo.PPayAgentApplyRequestVO;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.Result;
import com.elend.p2p.ServiceException;
import com.elend.p2p.constant.ResultCode;
import com.elend.pay.agent.sdk.constant.PayAgentChannel;
import com.elend.pay.agent.sdk.vo.UmbpayAccreditVO;
import com.elend.pay.agent.sdk.vo.UmbpayAgentVO;
import com.elend.pay.agent.sdk.vo.UmbpaySignWhiteListOneVO;

/**
 * 订单处理
 * @author liyongquan 2015年5月28日
 *
 */
@Component
public class OrderFacade {
    @Autowired
    private OrderService service;
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
    public Result<String> sendChargeRequest(ChannelIdConstant channelId,PartnerConstant partnerId,
                                     String partnerOrderId,BigDecimal amount,
                                     RequestFormData form,String ip,RequestSource source,
                                     String callbackUrl,String notifyUrl,String orderId, PayType payType){
        return service.sendChargeRequest(channelId, partnerId, partnerOrderId,
                                         amount, form, ip, source,callbackUrl,notifyUrl,orderId, payType);
    }
    /**
     * 充值回调
     * @param success--充值是否成功
     * @param channelId--渠道ID
     * @param callbackData--回调的数据(解析后)
     * @param params--支付网关回调参数(解析前)
     * @return
     */
    public Result<String> chargeCallback(String resultCode,ChannelIdConstant channelId,
                                  ChargeCallbackData callbackData,Map<String,String> params){
        try{
            return service.chargeCallback(resultCode, channelId, callbackData,params);
        }catch(ServiceException e){
            return new Result<String>(ResultCode.FAILURE, null, e.getMessage());
        }
    }
    /**
     * 获取充值请求
     * @param orderId--网关订单号
     * @return
     */
    public DepositRequest getChargeRequest(String orderId){
        return service.getChargeRequest(orderId);
    }
    
    /**
     * 更新通知状态
     * @param orderId--订单号
     * @param status--通知状态
     * @return
     */
    public Result<String> updateNotifyStatus(String orderId,NotifyStatus status){
        return service.updateNotifyStatus(orderId, status);
    }
    
    /**
     * 
     * @param data
     * @param partnerResult
     * @param orderId
     * @return
     */
    public Result<String> sendWithdrawRequest(PartnerWithdrawData data, String orderId) {
        return service.sendWithdrawRequest(data, orderId);
    }
    
    /**
     * 提现回调订单处理
     * @param success--第三方返回订单状态
     * @param channel--代付渠道
     * @param backData--第三方返回数据
     * @return
     */
    public Result<String> withdrawCallback(boolean success,
                                           WithdrawChannel channel, WithdrawCallbackData backData) {
        return service.withdrawCallback(success, channel, backData);
    }
    
    /**
     * 获取提现请求数据
     * @param orderId  网关订单号
     * @return
     */
    public PWithdrawRequestPO getWithdrawRequest(String orderId) {
        return service.getWithdrawRequest(orderId);
    }
    
    /**
     * 获取订单记录
     * @param partnerId
     * @param partnerOrderId
     * @return
     */
    public POrderPO getOrderBypartnerOrderId(PartnerConstant partnerId,
            String partnerOrderId) {
        return service.getOrderBypartnerOrderId(partnerId, partnerOrderId);
    }
    
    /**
     * 转账
     * @param sourceUserId      转出账号
     * @param targetUserId      转入账号
     * @param amount            金额
     * @return                  订单号
     */
    public Result<String> transfer(long sourceUserId, long targetUserId, BigDecimal amount, Subject subject, SubSubject subSubject, String remark) {
        return service.transfer(sourceUserId, targetUserId, amount, subject, subSubject, remark);
    }
    
    /**
     * 没有操作的账本的订单补单
     * @param startTime
     */
    public void repairBalanceAndLog(Date startTime) {
        service.repairBalanceAndLog(startTime);
    }
    
    /**
     * 调账
     * @param userId
     * @param amount
     * @return
     */
    public Result<String> adjust(long userId, BigDecimal amount) {
        return service.adjust(userId, amount);
    }
    
    /**
     * 代扣获取请求记录
     * @param data
     * @param orderId
     * @return
     */
    public Result<String> sendPayAgentGetCodeRequest(
            PartnerPayAgentGetCodeData data, String orderId) {
        return service.sendPayAgentGetCodeRequest(data, orderId);
    }
    
    /**
     * 代扣鉴权发送验证码回调订单处理
     * @param success
     * @param channel
     * @param object
     * @return
     */
    public Result<String> PayAgentGetCodeCallback(boolean success, String orderId,
            PayAgentChannel channel, UmbpayAccreditVO umbpayAccreditVO) {
        return service.PayAgentGetCodeCallback(success, orderId, channel, umbpayAccreditVO);
    }
    
    /**
     * 代扣鉴权校验验证码回调订单处理
     * @param data
     * @return
     */
    public Result<PPayAgentApplyRequestVO> sendPayAgentCheckCodeRequest(String orderId,
            PartnerPayAgentCheckCodeData data) {
        return service.sendPayAgentCheckCodeRequest(orderId, data);
    }
    
    /**
     * 代扣校验验证码回调处理
     * @param success
     * @param orderId
     * @param object
     * @return
     */
    public Result<String> PayAgentCheckCodeCallback(boolean success, PayAgentChannel channel,
            String orderId, UmbpaySignWhiteListOneVO backData) {
        return service.PayAgentCheckCodeCallback(success, channel, orderId, backData);
    }
    
    /**
     * 代收请求记录
     * @param data
     * @return
     */
    public Result<String> sendPayAgentChargeRequest(String orderId,
            PartnerPayAgentChargeData data) {
        return service.sendPayAgentChargeRequest(orderId, data);
    }
    
    /**
     * 代扣回调
     * @param success
     * @param channel
     * @param orderId
     * @param object
     * @return
     */
    public Result<String> PayAgentChargeCallback(boolean success,
            PayAgentChannel channel, String orderId, UmbpayAgentVO umbpayAgentVO) {
        return service.PayAgentChargeCallback(success, channel,  orderId, umbpayAgentVO);
    }
}
