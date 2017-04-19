package com.elend.gate.web.service;

import com.elend.gate.channel.constant.WithdrawChannel;
import com.elend.gate.channel.facade.vo.PartnerWithdrawRequest;
import com.elend.gate.channel.facade.vo.PartnerWithdrawResponse;
import com.elend.p2p.Result;

/**
 * 提现代付相关接口
 * @author liyongquan 2015年11月6日
 *
 */
public interface WithdrawService {
    /**
     * 提现请求
     * @param params    请求参数
     * @return
     */
    Result<PartnerWithdrawResponse> withdrawRequest(PartnerWithdrawRequest params);
    
    /**
     * 提现回调
     * @param channel--支付渠道
     * @param params--回调参数
     * @return  应答给第三方渠道的字符串
     */
    Result<String> withdrawCallback(WithdrawChannel channel, String backStr);
    
    /**
     * 和第三方同步
     * @param orderId
     * @return
     */
    Result<String> withdrawSync(String orderId);
}
