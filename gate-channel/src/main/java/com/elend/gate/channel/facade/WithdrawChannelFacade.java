package com.elend.gate.channel.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.elend.gate.channel.PartnerResult;
import com.elend.gate.channel.WithdrawChannelFactory;
import com.elend.gate.channel.constant.WithdrawChannel;
import com.elend.gate.channel.facade.vo.PartnerWithdrawData;
import com.elend.gate.channel.facade.vo.WithdrawCallbackData;
import com.elend.gate.channel.facade.vo.WithdrawSingleQueryData;
import com.elend.gate.channel.service.WithdrawChannelService;

/**
 * 提现渠道
 * @author mgt
 *
 */
@Component
public class WithdrawChannelFacade {
    private final static Logger logger = LoggerFactory.getLogger(WithdrawChannelFacade.class);
    
    /**
     * 单笔提现
     * @param channel   渠道
     * @param orderId   订单号
     * @param data      提现的数据
     * @return
     */
    public PartnerResult<WithdrawCallbackData> withdrawSingle(WithdrawChannel channel, String orderId, PartnerWithdrawData data){
        logger.info("channel:" + channel);
        WithdrawChannelService service = WithdrawChannelFactory.getPayChannel(channel);
        logger.info("service:" + service);
        return service.withdrawSingle(orderId, data);
    }

    /**
     * 提现回调
     * @param channel
     * @param backStr
     * @return
     */
    public PartnerResult<WithdrawCallbackData> withdrawCallback(
            WithdrawChannel channel, String backStr) {
        logger.info("channel:" + channel);
        WithdrawChannelService service = WithdrawChannelFactory.getPayChannel(channel);
        logger.info("service:" + service);
        return service.withdrawCallback(backStr);
    }
    
    /**
     * 单笔提现查询
     * @param channel   渠道
     * @param orderId   订单号
     * @return
     */
    public PartnerResult<WithdrawCallbackData> withdrawSingleQuery(WithdrawChannel channel, String orderId){
        logger.info("channel:" + channel);
        WithdrawChannelService service = WithdrawChannelFactory.getPayChannel(channel);
        logger.info("service:" + service);
        return service.withdrawSingleQuery(orderId);
    }
}
