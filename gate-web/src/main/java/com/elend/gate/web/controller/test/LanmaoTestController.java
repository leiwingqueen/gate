package com.elend.gate.web.controller.test;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.elend.gate.channel.facade.PayChannelFacade;
import com.elend.gate.web.controller.PayController;
import com.elend.gate.web.service.PayService;
import com.elend.p2p.BaseController;
import com.elend.p2p.gson.JSONUtils;
import com.elend.p2p.util.encrypt.DesPropertiesEncoder;

/**
 * 懒猫测试
 * @author mgt
 */
@Controller
@Scope("prototype")
public class LanmaoTestController extends BaseController{
    
    private final static Logger logger = LoggerFactory.getLogger(LanmaoTestController.class);
    
    @Autowired
    private PayChannelFacade payChannelFacade;
    @Autowired
    private PayService payService;
    
    @RequestMapping(value = "/lanmao/callback.jspx")
    @ResponseBody
    public String payPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        logger.info("demo callback");
        
        Enumeration<Object> em = request.getParameterNames();
        
        Map<String, Object> map = new HashMap<>();
        
        for (; em.hasMoreElements();) {
            Object o = em.nextElement();
            map.put(o.toString(), request.getParameter(o.toString()));
        }
        
        String json = JSONUtils.toJson(map, false);
        
        logger.info(json);
        
        //response.setContentType("application/text; charset=UTF-8");
        
        DesPropertiesEncoder encoder = new DesPropertiesEncoder();
        
        String encode = encoder.encode(json);
        
        logger.info(encode);
        
        return encode;
    }
    
    @RequestMapping(value = "/lanmao/submit.jspx")
    public ModelAndView submit(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("forward:/lanmaoTest.html");
    }
}
