package com.elend.gate.risk;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elend.gate.risk.vo.RiskParam;
import com.elend.p2p.Result;
import com.elend.p2p.constant.ResultCode;
import com.elend.p2p.gson.JSONUtils;

/**
 * 风控访问责任链
 * @author liyongquan 2015年6月8日
 *
 */
public class RiskHandlerChain {
    private final static Logger logger = LoggerFactory.getLogger(RiskHandlerChain.class);
    
    private List<RiskHandler> riskChain;
    /**
     * 风控处理
     * @return
     */
    public Result<String> handle(RiskParam request){
        logger.info("start risk handle...");
        for(RiskHandler riskHandler:riskChain){
            Result<String> result=riskHandler.handleRequest(request);
            if(!result.isSuccess()){
                logger.info("risk handle is done...result:{}",JSONUtils.toJson(result, false).toString());
                return result;
            }
        }
        logger.info("risk handle is done...result:success");
        return new Result<String>(ResultCode.SUCCESS);
    }
    public List<RiskHandler> getRiskChain() {
        return riskChain;
    }
    public void setRiskChain(List<RiskHandler> riskChain) {
        this.riskChain = riskChain;
    }
}
