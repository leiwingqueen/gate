package com.elend.gate.risk;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.elend.gate.risk.vo.RiskParam;
import com.elend.p2p.Result;
import com.elend.p2p.constant.ResultCode;

/**
 * 请求时间戳超时判断
 * @author liyongquan 2015年6月12日
 *
 */
@Component
public class TimeStampHandler implements RiskHandler{
    /**
     * 最大允许的时间间隔5min
     */
    private static final long MAX_INTERVAL=5*60*1000;
    @Override
    public Result<String> handleRequest(RiskParam request) {
        if(request.getTimeStamp()<=0){
            return new Result<String>(ResultCode.FAILURE,null, "时间戳不能小于0");
        }
        if(Math.abs(new Date().getTime()-request.getTimeStamp())>MAX_INTERVAL){
            return new Result<String>(ResultCode.FAILURE,null, "请求超时");
        }
        return new Result<String>(ResultCode.SUCCESS);
    }
}
