package com.elend.gate.risk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.conf.facade.PropertyFacade;
import com.elend.gate.context.AccessContext;
import com.elend.gate.risk.vo.RiskParam;
import com.elend.p2p.Result;
import com.elend.p2p.constant.ResultCode;
import com.elend.p2p.util.memcached.CounterMemcache;

/**
 * IP访问次数限制
 * @author liyongquan 2015年6月8日
 *
 */
@Component
public class IpCountHandler implements RiskHandler{
    private static final String KEY_IP_COUNT_KEY="gate_ip_count_";

    @Override
    public Result<String> handleRequest(RiskParam request) {
        int maxCount=propertyFacade.getInt("ip_count_persecond", 10);
        long ipCount=counterMemcache.addOrIncr(KEY_IP_COUNT_KEY+AccessContext.getAccessIp(), 1, 1);
        if(ipCount>maxCount){
            return new Result<String>(ResultCode.FAILURE, null,"IP访问次数过多");
        }
        return new Result<String>(ResultCode.SUCCESS);
    }
    @Autowired
    private CounterMemcache counterMemcache;
    @Autowired
    private PropertyFacade propertyFacade;
}
