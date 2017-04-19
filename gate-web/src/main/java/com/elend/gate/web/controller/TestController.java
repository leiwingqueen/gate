package com.elend.gate.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.elend.gate.channel.facade.vo.PartnerChargeRequest;
import com.elend.p2p.BaseController;
import com.elend.p2p.util.RequestLogUtil;

/**
 * 支付相关接口
 * @author liyongquan 2015年5月22日
 *
 */
@Controller
public class TestController extends BaseController{
    
    private final static Logger logger = LoggerFactory.getLogger(TestController.class);
    
    /**
     * 发起充值请求
     * @param request
     * @return
     */
    @RequestMapping(value = "/redirect1.jspx")
    public ModelAndView redirect1(HttpServletRequest request,PartnerChargeRequest params) {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        return new ModelAndView("redirect:https://test2.gzdai.com/redirect2.jspx", map);
    }
    
    
    @RequestMapping(value = "/redirect2.jspx")
    public ModelAndView redirect2(HttpServletRequest request,PartnerChargeRequest params) {
        RequestLogUtil.logParam(logger, request);
        return new ModelAndView("forward:/error.jsp");
    }
}
