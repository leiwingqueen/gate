package com.elend.gate.web.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
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
import org.springframework.web.servlet.view.RedirectView;

import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.exception.PayFailureException;
import com.elend.gate.channel.facade.vo.AuthPartnerChargeRequest;
import com.elend.gate.channel.facade.vo.PartnerChargeRequest;
import com.elend.gate.conf.facade.SystemConfig;
import com.elend.gate.util.FormUtil;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.gate.web.service.PayService;
import com.elend.gate.web.util.RequestUtil;
import com.elend.p2p.BaseController;
import com.elend.p2p.NoTraceStackException;
import com.elend.p2p.Result;
import com.elend.p2p.gson.JSONUtils;
import com.elend.p2p.util.RequestLogUtil;
import com.elend.p2p.util.encrypt.DesPropertiesEncoder;
import com.google.gson.reflect.TypeToken;

/**
 * 支付相关接口
 * @author liyongquan 2015年5月22日
 *
 */
@Controller
@Scope("prototype")
public class PayController extends BaseController{
    private final static Logger logger = LoggerFactory.getLogger(PayController.class);
    @Autowired
    private PayService payService;
    
    private DesPropertiesEncoder encoder = new DesPropertiesEncoder();
    
    @Autowired
    private SystemConfig systemConfig;
    
    /**
     * 发起充值请求
     * @param request
     * @return
     */
    @RequestMapping(value = "/pay.jspx")
    public ModelAndView payPage(HttpServletRequest request,PartnerChargeRequest params) {
    	logger.info("充值请求");
        RequestLogUtil.logParam(logger, request);
        Result<RequestFormData> result=payService.chargeRequest(params);
        if(!result.isSuccess()){
            //throw new NoTraceStackException(result.getMessage());
            logger.error("生成充值请求失败，原因:"+result.getMessage());
            Map<String,String> errorMap=new HashMap<String, String>();
            errorMap.put("error", "系统异常:"+result.getMessage());
            return new ModelAndView("forward:/error.jsp",errorMap);
        }
        String form=FormUtil.build(result.getObject());
        logger.info("表单字符串：{}" + encoder.encode(form));
        Map<String,String> map=new HashMap<String, String>();
        map.put("form", form);
        return new ModelAndView("forward:/WEB-INF/pay.jsp",map);
    }
    
    /**
     * 发起充值请求
     * @param request
     * @return
     */
    @RequestMapping(value = "/wapPay.jspx")
    public ModelAndView wapPayPage(HttpServletRequest request, AuthPartnerChargeRequest params) {
        logger.info("wap认证支付");
        RequestLogUtil.logParam(logger, request);
        Result<RequestFormData> result=payService.chargeRequest(params);
        if(!result.isSuccess()){
            //throw new NoTraceStackException(result.getMessage());
            logger.error("生成充值请求失败，原因:"+result.getMessage());
            Map<String,String> errorMap=new HashMap<String, String>();
            errorMap.put("error", "系统异常:"+result.getMessage());
            return new ModelAndView("forward:/wap_error.jsp",errorMap);
        }
        String form=FormUtil.build(result.getObject());
        logger.info("表单字符串：{}", encoder.encode(form));
        Map<String,String> map=new HashMap<String, String>();
        map.put("form", form);
        return new ModelAndView("forward:/WEB-INF/pay.jsp",map);
    }
    
    /**
     * 移动端sdk发起充值请求
     * @param request
     * @return
     */
    @RequestMapping(value = "/pay.do")
    @ResponseBody
    public Result<RequestFormData> authPay(HttpServletRequest request,AuthPartnerChargeRequest params) {
        RequestLogUtil.logParam(logger, request);
        Result<RequestFormData> result=payService.chargeRequest(params);
        return result;
    }
    
    /**
     * 易宝请求回调
     * @param request
     * @return
     */
    @RequestMapping(value = "/yeepay/callback.jspx")
    public ModelAndView yeepayCallback(HttpServletRequest request,HttpServletResponse response) {
        RequestLogUtil.logParam(logger, request);
        Map<String,String> params=RequestUtil.getParameterMap(request);
        Result<RequestFormData> result=payService.chargeCallback(ChannelIdConstant.YEEPAY, params);
        if(!result.isSuccess()){
            throw new NoTraceStackException(result.getMessage());
        }
        //点对点通知
        if(result.getObject().isNotify()){
            PrintWriter writer=null;
            try {
                writer=response.getWriter();
                writer.write("success");
            } catch (IOException e) {
                logger.error(e.getMessage(),e);
            }finally{
                if(writer!=null){
                    writer.flush();
                    writer.close();
                }
            }
            return null;
        }
        //页面跳转回调
        String form=FormUtil.build(result.getObject());
        Map<String,String> map=new HashMap<String, String>();
        map.put("form", form);
        return new ModelAndView("forward:/WEB-INF/callback.jsp",map);
    }
    
    /**
     * 宝易互通异步回调
     * @param request
     * @return
     */
    @RequestMapping(value = "/umbpay/notify.do")
    public ModelAndView umbpayNotify(HttpServletRequest request, HttpServletResponse response) {
    	logger.info("宝易互通异步回调");
        RequestLogUtil.logParam(logger, request);
        Map<String,String> params=RequestUtil.getParameterMap(request);
        Result<RequestFormData> result=payService.chargeCallback(ChannelIdConstant.UMBPAY, params);
        if(!result.isSuccess()){
            throw new NoTraceStackException(result.getMessage());
        }
        
        PrintWriter writer=null;
        try {
            writer=response.getWriter();
            writer.write("success=true");
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }finally{
            if(writer!=null){
                writer.flush();
                writer.close();
            }
        }
        return null;
    }
    
    /**
     * 宝易互通同步回调
     * @param request
     * @return
     */
    @RequestMapping(value = "/umbpay/callback.jspx")
    public ModelAndView umbpayCallback(HttpServletRequest request,HttpServletResponse response) {
    	logger.info("宝易互通同步回调");
        RequestLogUtil.logParam(logger, request);
        Map<String,String> params=RequestUtil.getParameterMap(request);
        Result<RequestFormData> result=payService.chargeCallback(ChannelIdConstant.UMBPAY, params);
        if(!result.isSuccess()){
            throw new NoTraceStackException(result.getMessage());
        }
        //页面跳转回调
        String form=FormUtil.build(result.getObject());
        Map<String,String> map=new HashMap<String, String>();
        map.put("form", form);
        logger.info("form表单字符串：{}", form);
        return new ModelAndView("forward:/WEB-INF/callback.jsp",map);
    }
    
    /**
     * 宝易互通异步回调
     * @param request
     * @return
     */
    @RequestMapping(value = "/baofooGate/notify.do")
    @ResponseBody
    public String baofooNotify(HttpServletRequest request, HttpServletResponse response) {
        
        logger.info("宝付异步回调");
        RequestLogUtil.logParam(logger, request);
        Map<String,String> params = RequestUtil.getParameterMap(request);
        params.put("sync", "false");
        Result<RequestFormData> result=payService.chargeCallback(ChannelIdConstant.BAOFOO_GATE, params);
        if(!result.isSuccess()){
            throw new NoTraceStackException(result.getMessage());
        }

        return "OK";
    }
    
    /**
     * 宝易互通同步回调
     * @param request
     * @return
     */
    @RequestMapping(value = "/baofooGate/callback.jspx")
    public ModelAndView baofooCallback(HttpServletRequest request,HttpServletResponse response) {
        logger.info("宝付同步回调");
        RequestLogUtil.logParam(logger, request);
        Map<String,String> params=RequestUtil.getParameterMap(request);
        params.put("sync", "true");
        
        Result<RequestFormData> result=payService.chargeCallback(ChannelIdConstant.BAOFOO_GATE, params);
        if(!result.isSuccess()){
            throw new NoTraceStackException(result.getMessage());
        }
        //页面跳转回调
        String form=FormUtil.build(result.getObject());
        Map<String,String> map=new HashMap<String, String>();
        map.put("form", form);
        logger.info("form表单字符串：{}", form);
        return new ModelAndView("forward:/WEB-INF/callback.jsp",map);
    }
    
    /**
     * 连连SDK异步回调
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/lianlianMobile/notify.do")
    @ResponseBody
    public String lianlianMobileNotify(HttpServletRequest request,HttpServletResponse response) {
        RequestLogUtil.logParam(logger, request);
        ServletInputStream inputStream;
        Map<String,String> params=new HashMap<String, String>(); 
        try {
            inputStream = request.getInputStream();
            params=llReadParams(inputStream);
        } catch (IOException e) {
            logger.error("读取参数失败，原因:"+e.getMessage(),e);
        }
        Result<RequestFormData> result=payService.chargeCallback(ChannelIdConstant.LIANLIAN_MOBILE, params);
        if(!result.isSuccess()){
            throw new NoTraceStackException(result.getMessage());
        }
        return "{\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"}";
    }
    
    /**
     * 连连SDK同步回调
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/lianlianMobile/callback.do")
    @ResponseBody
    @Deprecated
    //同步回调目前不再使用
    public Result<RequestFormData> lianlianMobileCallback(HttpServletRequest request,HttpServletResponse response) {
        RequestLogUtil.logParam(logger, request);
        ServletInputStream inputStream;
        Map<String,String> params=new HashMap<String, String>(); 
        try {
            inputStream = request.getInputStream();
            params=llReadParams(inputStream);
        } catch (IOException e) {
            logger.error("读取参数失败，原因:"+e.getMessage(),e);
        }
        Result<RequestFormData> result=payService.chargeCallback(ChannelIdConstant.LIANLIAN_MOBILE, params);
        return result;
    }
    
    
    
    /**
     * 双乾异步回调
     * @param request
     * @return
     */
    @RequestMapping(value = "/moneyMoreMore/notify.do")
    public ModelAndView moneyMoreMoreNotify(HttpServletRequest request,HttpServletResponse response) {
        logger.info("双乾异步回调");
        RequestLogUtil.logParam(logger, request);
        Map<String,String> params=RequestUtil.getParameterMap(request);
        Result<RequestFormData> result=payService.chargeCallback(ChannelIdConstant.MONEY_MORE_MORE, params);
        if(!result.isSuccess()){
            throw new NoTraceStackException(result.getMessage());
        }
        
        PrintWriter writer=null;
        try {
            writer=response.getWriter();
            writer.write("success=true");
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }finally{
            if(writer!=null){
                writer.flush();
                writer.close();
            }
        }
        return null;
    }
    
    /**
     * 双乾同步回调
     * @param request
     * @return
     */
    @RequestMapping(value = "/moneyMoreMore/callback.jspx")
    public ModelAndView moneyMoreMoreCallback(HttpServletRequest request,HttpServletResponse response) {
        logger.info("双乾同步回调");
        RequestLogUtil.logParam(logger, request);
        Map<String,String> params=RequestUtil.getParameterMap(request);
        Result<RequestFormData> result=payService.chargeCallback(ChannelIdConstant.MONEY_MORE_MORE, params);
        if(!result.isSuccess()){
            throw new NoTraceStackException(result.getMessage());
        }
        //页面跳转回调
        String form=FormUtil.build(result.getObject());
        Map<String,String> map=new HashMap<String, String>();
        map.put("form", form);
        logger.info("form表单字符串：{}", form);
        return new ModelAndView("forward:/WEB-INF/callback.jsp",map);
    }
    
    /**
     * 连连wap回调
     * @param request
     * @return
     */
    @RequestMapping(value = "/lianlianWap/callback.jspx")
    public ModelAndView lianlianwapCallback(HttpServletRequest request,HttpServletResponse response) {
        logger.info("连连wap同步回调");
        RequestLogUtil.logParam(logger, request);
        Map<String,String> params=RequestUtil.getParameterMap(request);
        Result<RequestFormData> result = null;
        try {
            result = payService.chargeCallback(ChannelIdConstant.LIANLIAN_WAP, params);
        } catch (PayFailureException e) {
            logger.info("支付失败，跳转m版投标页面");
            //支付失败，跳转p2p,因为连连同步支付失败没有返回订单号，所以不做订单的处理，只返回页面展示给用户
            if(systemConfig.isTest()) {
                //return new ModelAndView(new RedirectView("https://mtest.gzdai.com/m/rechargeFail.jspx"));
                return new ModelAndView(new RedirectView("http://mtest.gzdai.com/mobile/user/rechargeFail.html"));
            } else {
                return new ModelAndView(new RedirectView("https://m.gzdai.com/mobile/user/rechargeFail.html"));
            }
        }
        if(!result.isSuccess()){
            if(result.getObject() == null) { //失败，不回调P2P
                throw new NoTraceStackException(result.getMessage());
            }
        }
        //页面跳转回调
        String formStr = FormUtil.build(result.getObject());
        Map<String,String> map=new HashMap<String, String>();
        map.put("form", formStr);
        return new ModelAndView("forward:/WEB-INF/callback.jsp",map);
    }
    
    /**
     * 连连wap异步回调
     * @param request
     * @return
     */
    @RequestMapping(value = "/lianlianWap/notify.do")
    @ResponseBody
    public String lianlianwapNotify(HttpServletRequest request,HttpServletResponse response) {
        
        logger.info("连连wap异步回调");
        RequestLogUtil.logParam(logger, request);
        StringBuffer strBuffer = new StringBuffer(2000);
        try {
            request.setCharacterEncoding("utf-8");
            BufferedReader reader = request.getReader();
            String temp = "";
            while ((temp = reader.readLine()) != null) {
                strBuffer.append(temp);
                if (strBuffer.length() > 500000) {
                    log.info("返回报文长度超过500000");
                    break;
                }
            }
        } catch (IOException e) {
            log.error("/lianlian/notify.do get request error", e);
            return "";
        }
        log.info("连连wap支付回调...参数:{}",strBuffer.toString());
        
        Map<String, String> params = new HashMap<>();
        params.put("res_data", strBuffer.toString());
        
        Result<RequestFormData> result=payService.chargeCallback(ChannelIdConstant.LIANLIAN_WAP, params);
        if(!result.isSuccess()){
            throw new NoTraceStackException(result.getMessage());
        }
        return "{\"ret_code\":\"0000\",\"ret_msg\":\"交易成功\"}";
    }
    
    
    /**
     * 连连gate回调
     * @param request
     * @return
     */
    @RequestMapping(value = "/lianlianGate/callback.jspx")
    public ModelAndView lianlianGateCallback(HttpServletRequest request,HttpServletResponse response) {
        logger.info("连连网银支付同步回调");
        RequestLogUtil.logParam(logger, request);
        Map<String,String> params=RequestUtil.getParameterMap(request);
        
        //判断是否有返回的参数
        if(params == null || params.keySet().size() < 1) {
            logger.info("回调的参数为空，不处理，直接返回, params:{}", params);
            throw new NoTraceStackException("未知的支付结果");
        }
        
        Result<RequestFormData> result=payService.chargeCallback(ChannelIdConstant.LIANLIAN_GATE, params);
        if(!result.isSuccess()){
            throw new NoTraceStackException(result.getMessage());
        }
        //页面跳转回调
        String form=FormUtil.build(result.getObject());
        Map<String,String> map=new HashMap<String, String>();
        map.put("form", form);
        logger.info("连连网银支付form表单字符串：{}", form);
        return new ModelAndView("forward:/WEB-INF/callback.jsp",map);
    }
    
    /**
     * 连连gate异步回调
     * @param request
     * @return
     * @throws IOException 
     */
    @RequestMapping(value = "/lianlianGate/notify.do")
    @ResponseBody
    public Map<String, String> lianlianGateNotify(HttpServletRequest request,HttpServletResponse response) throws IOException {
        
        logger.info("连连网银支付异步回调");
        
        Map<String, String> params = llReadParams(request.getInputStream());
        logger.info("连连网银支付, paras:{}", params);
        
        Map<String, String> map = new HashMap<>();

        if(params == null || params.keySet().size() < 1) {
            logger.info("回调的参数为空，不处理，直接返回, params:{}", params);
            map.put("ret_code", "-1");
            map.put("ret_msg", "获取参数失败");
            return map;
        }
        
        Result<RequestFormData> result = payService.chargeCallback(ChannelIdConstant.LIANLIAN_GATE, params);
        
        if(!result.isSuccess()){
            map.put("ret_code", "-1");
            map.put("ret_msg", result.getMessage());
        } else {
            map.put("ret_code", "0000");
            map.put("ret_msg", "交易成功");
        }
        
        return map;
    }
    
    
    /**
     * 连连支付读取参数
     * @param inputStream--输入流
     * @return
     */
    private Map<String,String> llReadParams(InputStream inputStream){
        String content="";
        Map<String,String> params = null;
        try {
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();  
            byte[] buffer = new byte[1024];  
            int len = -1;  
            while ((len = inputStream.read(buffer)) != -1) {  
                outSteam.write(buffer, 0, len);  
            }  
            outSteam.close();  
            inputStream.close();  
            content=outSteam.toString("UTF-8");
            params=JSONUtils.fromJson(content, new TypeToken<Map<String,String>>(){});
        } catch (IOException e) {
            logger.error("读取参数失败，原因:"+e.getMessage(),e);
        }
        return params;
    }
}
