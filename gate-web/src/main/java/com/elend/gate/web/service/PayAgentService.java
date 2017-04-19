package com.elend.gate.web.service;

import com.elend.gate.channel.facade.vo.PartnerPayAgentChargeRequest;
import com.elend.gate.channel.facade.vo.PartnerPayAgentChargeResponse;
import com.elend.gate.channel.facade.vo.PartnerPayAgentCheckCodeRequest;
import com.elend.gate.channel.facade.vo.PartnerPayAgentCheckCodeResponse;
import com.elend.gate.channel.facade.vo.PartnerPayAgentGetCodeRequest;
import com.elend.gate.channel.facade.vo.PartnerPayAgentGetCodeResponse;
import com.elend.p2p.Result;


/**
 * 代扣
 * @author mgt
 *
 */
public interface PayAgentService {

    /**
     * 获取验证码
     * @param params
     * @return
     */
    Result<PartnerPayAgentGetCodeResponse> getCode(PartnerPayAgentGetCodeRequest params);

    /**
     * 校验校验码
     * @param params
     * @return
     */
    Result<PartnerPayAgentCheckCodeResponse> checkCode(
            PartnerPayAgentCheckCodeRequest params);

    /**
     * 待收操作
     * @param params
     * @return
     */
    Result<PartnerPayAgentChargeResponse> charge(
            PartnerPayAgentChargeRequest params);
}
