package com.elend.gate.web.service;

import java.util.Map;

import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.facade.vo.PartnerChargeRequest;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.Result;

/**
 * 支付相关接口
 * @author liyongquan 2015年6月2日
 *
 */
public interface PayService {
    /**
     * 充值请求
     * @param params--传入参数
     * @return
     */
    Result<RequestFormData> chargeRequest(PartnerChargeRequest params);
    
    /**
     * 充值回调
     * @param channel--支付渠道
     * @param params--回调参数
     * @return
     */
    Result<RequestFormData> chargeCallback(ChannelIdConstant channel,Map<String,String> params);
}
