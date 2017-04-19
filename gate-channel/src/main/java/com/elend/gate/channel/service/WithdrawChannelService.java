package com.elend.gate.channel.service;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.elend.gate.channel.PartnerResult;
import com.elend.gate.channel.constant.BankIdConstant;
import com.elend.gate.channel.constant.WithdrawChannel;
import com.elend.gate.channel.facade.vo.PartnerWithdrawData;
import com.elend.gate.channel.facade.vo.WithdrawCallbackData;
import com.elend.gate.channel.facade.vo.WithdrawSingleQueryData;
import com.elend.gate.channel.mapper.CBankIdConfigMapper;

/**
 * 提现渠道接口
 * @author mgt
 *
 */
public abstract class WithdrawChannelService {
    
    @Autowired
    private CBankIdConfigMapper bankConfigMapper;

    /**
     * 单笔提现
     * @param orderId
     * @param data
     * @return
     */
    public abstract PartnerResult<WithdrawCallbackData> withdrawSingle(String orderId, PartnerWithdrawData data);
    
    /**
     * 提现渠道ID
     * @return
     */
    public abstract  WithdrawChannel getChannelId();
    
    /**
     * 获取支付渠道银行ID
     * @param bankId
     * @return
     */
    protected String getChannelBankId(BankIdConstant bankId){
        if(BankIdConstant.NO_DESIGNATED==bankId)
            return "";
        String channelBank = bankConfigMapper.getChannelBankId(getChannelId().name(), bankId.getBankId());
        if(StringUtils.isBlank(channelBank))
            return "";
        return channelBank;
    }
    
    /**
     * 手续费计算
     * @param amount--充值金额
     */
    public abstract BigDecimal feeCalculate(BigDecimal amount);

    /**
     * 提现回调
     * @param backStr
     * @return
     */
    public abstract PartnerResult<WithdrawCallbackData> withdrawCallback(String backStr);

    /**
     * 单笔提现查询
     * @param orderId
     * @return
     */
    public abstract PartnerResult<WithdrawCallbackData> withdrawSingleQuery(String orderId);
}
