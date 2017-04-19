package com.elend.gate.channel.service;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.elend.gate.channel.PartnerResult;
import com.elend.gate.channel.constant.BankIdConstant;
import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.constant.PayType;
import com.elend.gate.channel.exception.CheckSignatureException;
import com.elend.gate.channel.exception.ParamException;
import com.elend.gate.channel.facade.vo.ChargeCallbackData;
import com.elend.gate.channel.facade.vo.PartnerChargeData;
import com.elend.gate.channel.facade.vo.PartnerSingleQueryChargeData;
import com.elend.gate.channel.mapper.CBankIdConfigMapper;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.ServiceException;

/**
 * 支付渠道接口
 *      所有支付渠道都必须实现这个接口
 * @author liyongquan 2015年5月21日
 *
 */
public abstract class PayChannelService {
    @Autowired
    private CBankIdConfigMapper bankConfigMapper;
    /**
     * 充值
     * @param orderId--订单号
     * @param chargeData--充值相关参数
     * 
     * @throws ParamException--一些特殊的参数要求可以在这里抛出，像认证支付的一些参数通过这里来约定
     * @return
     */
    public abstract RequestFormData charge(String orderId,PartnerChargeData chargeData);
    /**
     * 充值回调
     * @param channelId--渠道ID
     * @param params--回调参数
     * @return
     * @throws CheckSignatureException--签名验证失败
     * @throws ServiceException--回调处理发生的异常
     */
    public abstract PartnerResult<ChargeCallbackData> chargeCallback(ChannelIdConstant channelId,
                                              Map<String,String> params)throws CheckSignatureException,ServiceException;
    /**
     * 支付渠道ID
     * @return
     */
    public abstract  ChannelIdConstant getChannelId();
    
    /**
     * 获取银行ID
     * @param channelBankId
     * @return
     */
    protected String getBankId(String channelBankId){
        String bankId=bankConfigMapper.getBankId(getChannelId().name(), channelBankId);
        if(StringUtils.isBlank(bankId))return "";
        return bankId;
    }
    
    /**
     * 获取支付渠道银行ID
     * @param bankId
     * @return
     */
    protected String getChannelBankId(BankIdConstant bankId){
        if(BankIdConstant.NO_DESIGNATED==bankId)return "";
        String channelBank=bankConfigMapper.getChannelBankId(getChannelId().name(), bankId.getBankId());
        if(StringUtils.isBlank(channelBank))return "";
        return channelBank;
    }
    
    /**
     * 单笔充值查询
     * @param channelId  渠道
     * @param orderId	订单号
     * @return
     * @throws CheckSignatureException
     * @throws ServiceException
     */
    public abstract PartnerResult<PartnerSingleQueryChargeData> singleQueryCharge(ChannelIdConstant channelId, String orderId)throws CheckSignatureException,ServiceException;
    /**
     * 手续费计算
     * @param amount--充值金额
     */
    public abstract BigDecimal feeCalculate(BigDecimal amount, PayType payType);
}
