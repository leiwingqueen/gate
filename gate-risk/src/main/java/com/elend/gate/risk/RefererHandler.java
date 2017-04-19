package com.elend.gate.risk;

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
 * referer白名单限制
 * @author liyongquan 2015年6月8日
 *
 */
@Component
public class RefererHandler implements RiskHandler{
    private final static Logger logger = LoggerFactory.getLogger(RefererHandler.class);
    /**
     * 遍历访问的白名单检查referer是否合法
     */
    @Override
    public Result<String> handleRequest(RiskParam request) {
        String whiteLists=propertyFacade.getProperty("referer_white_list",
                                                     "www.gzdai.com,test.gzdai.com,www.yeepay.com");
        String referer=AccessContext.getReferer();
        for(String whiteList:whiteLists.split(",")){
            if("*".equals(whiteList)||whiteList.equals(getDomain(referer))){
                return new Result<String>(ResultCode.SUCCESS);
            }
        }
        logger.error("访问来源http referer不在白名单内,referer:{},ip:{}"+referer,AccessContext.getAccessIp());
        return new Result<String>(ResultCode.FAILURE,null,"访问失败");
    }
    /**
     * 获取referer的域
     * @param referer
     * @return
     */
    public String getDomain(String referer){
        int index=referer.indexOf("http://");
        int index2=referer.indexOf("https://");
        String domin=referer;
        if(index>=0){
            domin=referer.substring("http://".length());
        }else if(index2>=0){
            domin=referer.substring("https://".length());
        }
        index=domin.indexOf("/");
        if(index>=0){
            domin=domin.substring(0,index);
        }
        return domin;
    }
    @Autowired
    private PropertyFacade propertyFacade;
}
