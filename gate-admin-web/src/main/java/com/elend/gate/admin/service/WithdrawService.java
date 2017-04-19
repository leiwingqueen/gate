package com.elend.gate.admin.service;

import com.elend.p2p.Result;

/**
 * 提现代付相关接口
 * @author liyongquan 2015年11月6日
 *
 */
public interface WithdrawService {
    /**
     * 和第三方同步
     * @param orderId
     * @return
     */
    Result<String> withdrawSync(String orderId);
}
