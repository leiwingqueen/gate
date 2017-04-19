package com.elend.gate.risk;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.conf.facade.PropertyFacade;
import com.elend.gate.context.AccessContext;
import com.elend.gate.risk.vo.RiskParam;
import com.elend.p2p.Result;
import com.elend.p2p.constant.ResultCode;

/**
 * IP一致性检查
 * @author liyongquan 2015年6月12日
 *
 */
@Component
public class IpConsistentHandler implements RiskHandler{
    private final static Logger logger = LoggerFactory.getLogger(IpConsistentHandler.class);
    @Autowired
    private PropertyFacade propertyFacade;

    @Override
    public Result<String> handleRequest(RiskParam request) {
        //IP限制开关
        boolean flag=propertyFacade.getBoolean("risk_ip_consistent",true);
        if(!flag){
            return new Result<String>(ResultCode.SUCCESS);
        }
        if(StringUtils.isBlank(request.getIp())){
            return new Result<String>(ResultCode.FAILURE, null, "IP不能为空");
        }
        logger.info("用户的来源IP：{}，参数传入的IP:{}",AccessContext.getAccessIp(),request.getIp());
        if(!AccessContext.getAccessIp().equals(request.getIp())){
            return new Result<String>(ResultCode.FAILURE, null, "访问IP不一致");
        }
        return new Result<String>(ResultCode.SUCCESS);
    }
}
