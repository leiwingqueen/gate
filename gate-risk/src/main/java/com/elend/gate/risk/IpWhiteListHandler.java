package com.elend.gate.risk;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.conf.facade.PropertyFacade;
import com.elend.gate.risk.vo.RiskParam;
import com.elend.p2p.Result;
import com.elend.p2p.constant.ResultCode;

/**
 * IP白名单检查(代付使用)
 * @author liyongquan 2015年11月12日
 *
 */
@Component
public class IpWhiteListHandler implements RiskHandler{
    private final static Logger logger = LoggerFactory.getLogger(IpWhiteListHandler.class);
    @Autowired
    private PropertyFacade propertyFacade;

    @Override
    public Result<String> handleRequest(RiskParam request) {
        String whiteList=propertyFacade.getProperty("ip_white_list","");
        //空串认为不做IP白名单控制
        if(StringUtils.isBlank(whiteList)){
            return new Result<String>(ResultCode.SUCCESS);
        }
        for(String ip:whiteList.split(",")){
            if(request.getIp().trim().equals(ip.trim())){
                return new Result<String>(ResultCode.SUCCESS);
            }
        }
        logger.error("访问不在IP白名单中，拒绝请求,请求IP:{},白名单:{}",request.getIp(),whiteList);
        return new Result<String>(ResultCode.FAILURE,null,"请求异常");
    }
}
