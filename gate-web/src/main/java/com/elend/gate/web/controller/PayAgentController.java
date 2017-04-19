package com.elend.gate.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.elend.gate.channel.facade.vo.PartnerPayAgentChargeRequest;
import com.elend.gate.channel.facade.vo.PartnerPayAgentChargeResponse;
import com.elend.gate.channel.facade.vo.PartnerPayAgentCheckCodeRequest;
import com.elend.gate.channel.facade.vo.PartnerPayAgentCheckCodeResponse;
import com.elend.gate.channel.facade.vo.PartnerPayAgentGetCodeRequest;
import com.elend.gate.channel.facade.vo.PartnerPayAgentGetCodeResponse;
import com.elend.gate.web.service.PayAgentService;
import com.elend.p2p.BaseController;
import com.elend.p2p.Result;
import com.elend.p2p.util.RequestLogUtil;

/**
 * 代收相关接口
 * @author mgt
 *
 */
@Controller
@Scope("prototype")
public class PayAgentController extends BaseController{
    
    private final static Logger logger = LoggerFactory.getLogger(PayAgentController.class);
    
    @Autowired
    private PayAgentService payAgentService;
    
    /**
     * 鉴权获取验证码
     * @param request
     * @return
     */
    @RequestMapping(value = "/payAgent/getCode.do")
    @ResponseBody
    public Result<PartnerPayAgentGetCodeResponse> getCode(HttpServletRequest request, PartnerPayAgentGetCodeRequest params) {
        logger.info("代扣获取验证码...");
        RequestLogUtil.logParam(logger, request);
        Result<PartnerPayAgentGetCodeResponse> result = payAgentService.getCode(params);
        return result;
    }
    
    /**
     * 鉴权检查验证码
     * @param request
     * @return
     */
    @RequestMapping(value = "/payAgent/checkCode.do")
    @ResponseBody
    public Result<PartnerPayAgentCheckCodeResponse> checkCode(HttpServletRequest request, PartnerPayAgentCheckCodeRequest params) {
        logger.info("代扣校验验证码...");
        RequestLogUtil.logParam(logger, request);
        Result<PartnerPayAgentCheckCodeResponse> result = payAgentService.checkCode(params);
        return result;
    }
    
    /**
     * 代扣
     * @param request
     * @return
     */
    @RequestMapping(value = "/payAgent/charge.do")
    @ResponseBody
    public Result<PartnerPayAgentChargeResponse> charge(HttpServletRequest request, PartnerPayAgentChargeRequest params) {
        logger.info("代收接口...");
        RequestLogUtil.logParam(logger, request);
        Result<PartnerPayAgentChargeResponse> result = payAgentService.charge(params);
        return result;
    }
}
