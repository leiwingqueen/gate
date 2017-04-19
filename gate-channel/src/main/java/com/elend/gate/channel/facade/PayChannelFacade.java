package com.elend.gate.channel.facade;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.elend.gate.channel.ChannelFactory;
import com.elend.gate.channel.PartnerResult;
import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.constant.PayType;
import com.elend.gate.channel.exception.CheckSignatureException;
import com.elend.gate.channel.facade.vo.ChargeCallbackData;
import com.elend.gate.channel.facade.vo.PartnerChargeData;
import com.elend.gate.channel.facade.vo.PartnerSingleQueryChargeData;
import com.elend.gate.channel.service.PayChannelService;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.ServiceException;

@Component
public class PayChannelFacade {
    private final static Logger logger = LoggerFactory.getLogger(PayChannelFacade.class);
    
    /**
     * 充值
     * @param channel--支付渠道
     * @param orderId--订单号
     * @param amount--充值金额
     * @param bankId--银行ID(网关定义，跟具体的支付渠道无关)
     * 
     * @param userId--用户ID(认证支付使用)
     * @param realName--真实姓名(认证支付使用)
     * @param idCard--身份证(认证支付使用)
     * @param bankAccount--银行卡号(认证支付使用)
     * @return
     */
    public RequestFormData charge(ChannelIdConstant channel,
            String orderId,PartnerChargeData chargeData){
    	logger.info("channel:" + channel);
        PayChannelService service=ChannelFactory.getPayChannel(channel);
        logger.info("service:" + service);
        return service.charge(orderId, chargeData);
    }
    
    /**
     * 充值回调
     * @param channelId--渠道ID
     * @param params--回调参数
     * @return
     * @throws CheckSignatureException--签名验证失败
     * @throws ServiceException--回调处理发生的异常
     */
    public PartnerResult<ChargeCallbackData> chargeCallback(ChannelIdConstant channelId,
                                              Map<String,String> params)throws CheckSignatureException,ServiceException{
        PayChannelService service=ChannelFactory.getPayChannel(channelId);
        PartnerResult<ChargeCallbackData> partnerResult=service.chargeCallback(channelId, params);
        return partnerResult;
    }
    
    /**
     * 单笔充值查询
     * @param channelId  渠道
     * @param orderId	订单号
     * @return
     * @throws CheckSignatureException
     * @throws ServiceException
     */
    public PartnerResult<PartnerSingleQueryChargeData> singleQueryCharge(ChannelIdConstant channelId, String orderId)throws CheckSignatureException,ServiceException {
    	PayChannelService service=ChannelFactory.getPayChannel(channelId);
        return service.singleQueryCharge(channelId, orderId);
    }
    
   /**
    * 手续费计算
    * @param channelId--渠道ID
    * @param amount--金额
    * @return
    */
    public BigDecimal feeCalculate(ChannelIdConstant channelId,BigDecimal amount, PayType payType){
        PayChannelService service=ChannelFactory.getPayChannel(channelId);
        return service.feeCalculate(amount, payType);
    }
}
