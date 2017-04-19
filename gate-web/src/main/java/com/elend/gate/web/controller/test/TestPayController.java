package com.elend.gate.web.controller.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.facade.PayChannelFacade;
import com.elend.gate.channel.facade.vo.PartnerChargeData;
import com.elend.gate.util.FormUtil;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.gate.web.service.PayService;
import com.elend.gate.web.util.RequestUtil;
import com.elend.p2p.BaseController;
import com.elend.p2p.NoTraceStackException;
import com.elend.p2p.Result;
import com.elend.p2p.ServiceException;
import com.elend.p2p.util.OrderIdHelper;
import com.elend.p2p.util.RequestLogUtil;

/**
 * 支付相关接口
 * @author liyongquan 2015年5月22日
 *
 */
@Controller
@Scope("prototype")
public class TestPayController extends BaseController{
    @Autowired
    private PayChannelFacade payChannelFacade;
    @Autowired
    private PayService payService;
    
    @RequestMapping(value = "/test/pay.jspx")
    public ModelAndView payPage(HttpServletRequest request) {
        String orderId=OrderIdHelper.newOrderId();
        PartnerChargeData chargeData=new PartnerChargeData();
        chargeData.setAmount(new BigDecimal("0.01"));
        RequestFormData formData=payChannelFacade.charge(ChannelIdConstant.YEEPAY, orderId, chargeData);
        String form=FormUtil.build(formData);
        Map<String,String> map=new HashMap<String, String>();
        map.put("form", form);
        return new ModelAndView("forward:/WEB-INF/yeepay/pay.jsp",map);
    }
    
    
    @RequestMapping(value = "/test/moneyMoremorepay.jspx")
    public ModelAndView moneyMoremorepay(HttpServletRequest request) {
        String orderId=OrderIdHelper.newOrderId();
        PartnerChargeData chargeData=new PartnerChargeData();
        chargeData.setAmount(new BigDecimal("0.01"));
        RequestFormData formData=payChannelFacade.charge(ChannelIdConstant.MONEY_MORE_MORE, orderId, chargeData);
        String form=FormUtil.build(formData);
        Map<String,String> map=new HashMap<String, String>();
        map.put("form", form);
        log.info("传参的form  {}",form);
        return new ModelAndView("forward:/WEB-INF/pay.jsp",map); 
    }
    
    
    
    @RequestMapping(value = "/test/moneyMoreMore/callback.jspx")
    public ModelAndView moneyMoreMoreCallback(HttpServletRequest request) {
        log.info("双乾同步回调");
//        RequestLogUtil.logParam(log, request);
//        Map<String,String> params=RequestUtil.getParameterMap(request);      
        Map<String, String> params=new HashMap<String, String>();
        params.put("MerNo", "168885");
        params.put("Result", "交易成功");
        params.put("Amount", "0.01");
        params.put("Succeed", "88");
        params.put("Orderno", "16888515102910243491866");
        params.put("MerRemark", "广州易贷");
        params.put("MD5info", "17DA7CB9054052471B05E2961D47A459");
        params.put("BillNo", "15102910234700257142");      
        Result<RequestFormData> result=payService.chargeCallback(ChannelIdConstant.MONEY_MORE_MORE, params);
        if(!result.isSuccess()){
            throw new NoTraceStackException(result.getMessage());
        }
        //页面跳转回调
        String form=FormUtil.build(result.getObject());
        Map<String,String> map=new HashMap<String, String>();
        map.put("form", form);
        log.info("form表单字符串：{}", form);
        return new ModelAndView("forward:/WEB-INF/callback.jsp",map);
    }
    
    
    
    /**
     * 不需要通过签名加密等方式访问
     * @param request
     * @param channelId
     * @param orderId
     * @param amount
     * @return
     */
    @RequestMapping(value = "/pay1.jspx")
    public ModelAndView payPage(HttpServletRequest request,@RequestParam(required=true)String channelId,
            @RequestParam(required=true)String orderId,@RequestParam(required=true)String amount) {
        ChannelIdConstant channel=null;
        try{
            channel=ChannelIdConstant.from(channelId);
        }catch(IllegalArgumentException e){
            throw new ServiceException("找不到对应的支付渠道");
        }
        BigDecimal amt=BigDecimal.ZERO;
        try{
            amt=new BigDecimal(amount);
        }catch(NumberFormatException e){
            throw new ServiceException("金额格式错误");
        }
        PartnerChargeData chargeData=new PartnerChargeData();
        chargeData.setAmount(amt);
        RequestFormData formData=payChannelFacade.charge(channel, orderId,chargeData);
        String form=FormUtil.build(formData);
        Map<String,String> map=new HashMap<String, String>();
        map.put("form", form);
        return new ModelAndView("forward:/WEB-INF/yeepay/pay.jsp",map);
    }
    
    @RequestMapping(value = "/test/callback.jspx")
    public ModelAndView yeepayCallback(HttpServletRequest request) {
        Map<String,String> params=RequestUtil.getParameterMap(request);
        payChannelFacade.chargeCallback(ChannelIdConstant.YEEPAY, params);
        return new ModelAndView("forward:/WEB-INF/yeepay/callback.jsp");
    }
    
    @RequestMapping(value = "/test/callback1.jspx")
    public ModelAndView yeepayCallback1(HttpServletRequest request,HttpServletResponse response) {
        PrintWriter writer=null;
        try {
            writer=response.getWriter();
            writer.write("success");
        } catch (IOException e) {
            //logger.error(e.getMessage(),e);
        }finally{
            if(writer!=null){
                writer.flush();
                writer.close();
            }
        }
        return null;
    }
}
