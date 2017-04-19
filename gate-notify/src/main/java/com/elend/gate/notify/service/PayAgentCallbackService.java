package com.elend.gate.notify.service;

import com.elend.p2p.Result;

/**
 * 代收回调P2P
 * @author mgt
 *
 */
public interface PayAgentCallbackService {

    /**
     * 查询第三方状态并回调
     * @return
     */
    Result<String> queryAndCallback();
}
