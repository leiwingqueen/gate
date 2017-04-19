package com.elend.gate.channel.service;

import com.elend.gate.channel.vo.CBankIdConfigSearchVO;
import com.elend.gate.channel.vo.CBankIdConfigVO;
import com.elend.p2p.PageInfo;
import com.elend.p2p.Result;


/**
 * 渠道银行配置
 * @author mgt
 */
public interface CBankIdConfigService {

    /**
     * 充值银行列表
     * @param svo
     * @return
     */
    Result<PageInfo<CBankIdConfigVO>> list(CBankIdConfigSearchVO svo);
}
