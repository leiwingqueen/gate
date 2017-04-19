package com.elend.gate.risk;

import com.elend.gate.risk.vo.RiskParam;
import com.elend.p2p.Result;

/**
 * 风控处理
 * @author liyongquan 2015年6月8日
 *
 */
public interface RiskHandler {
    /**
     * 风控处理
     * @return 执行返回结果,如果当前类处理成功则不会继续往下一个处理类尝试
     */
    Result<String> handleRequest(RiskParam request);
}
