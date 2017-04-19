package com.elend.gate.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.elend.gate.channel.constant.WithdrawChannel;
import com.elend.gate.channel.facade.vo.PartnerWithdrawRequest;
import com.elend.gate.channel.facade.vo.PartnerWithdrawResponse;
import com.elend.gate.web.service.WithdrawService;
import com.elend.p2p.BaseController;
import com.elend.p2p.Result;
import com.elend.p2p.util.RequestLogUtil;
import com.elend.p2p.util.encrypt.DesPropertiesEncoder;

/**
 * 代付相关接口
 * @author liyongquan 2015年5月22日
 *
 */
@Controller
@Scope("prototype")
public class WithdrawController extends BaseController{
    private final static Logger logger = LoggerFactory.getLogger(WithdrawController.class);
    @Autowired
    private WithdrawService withdrawService;
    /**
     * 发起提现请求
     * @param request
     * @return
     */
    @RequestMapping(value = "/withdraw.do")
    @ResponseBody
    public Result<PartnerWithdrawResponse> withdraw(HttpServletRequest request, PartnerWithdrawRequest params) {
        logger.info("提现请求");
        RequestLogUtil.logParam(logger, request);
        Result<PartnerWithdrawResponse> result = withdrawService.withdrawRequest(params);
        return result;
    }
    
    /**
     * 宝易互通代付异步回调
     * @param request
     * @return
     */
    @RequestMapping(value = "/umbpay/withdrawNotify.do")
    public void umbpayWithdrawNotify(HttpServletRequest request, HttpServletResponse response) {
        log.info("进入宝易互通打款回调");
        StringBuffer strBuffer = new StringBuffer(2000);
        String backStr = "";
        try {
            request.setCharacterEncoding("utf-8");
            BufferedReader reader = request.getReader();
            String temp = "";
            while ((temp = reader.readLine()) != null) {
                strBuffer.append(temp);
                if (strBuffer.length() > 500000) {
                    log.error("返回报文长度超过500000");
                    return ;
                }
            }
            backStr = URLDecoder.decode(strBuffer.toString(), "utf-8");
        } catch (IOException e) {
            log.error("接收回调参数异常", e);
            return ;
        }
        log.info("民生代付回调...参数:{}", new DesPropertiesEncoder().encode(backStr));
        
        Result<String> result = withdrawService.withdrawCallback(WithdrawChannel.UMBPAY_WITHDRAW, backStr);
        if(!result.isSuccess()){
            return;
        }
        
        PrintWriter writer=null;
        try {
            response.setContentType("text/html;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            writer=response.getWriter();
            log.info("提现回调响应报文:{}", result.getObject());
            writer.write(result.getObject());
        } catch (Exception e) {
            log.error("umbpay withdrawCallback response error");
        }finally{
            if(writer!=null){
                writer.flush();
                writer.close();
            }
        }
    }
    
    /**
     * 易宝代付异步回调
     * @param request
     * @return
     */
    @RequestMapping(value = "/yeepay/withdrawNotify.do")
    public void yeepayWithdrawNotify(HttpServletRequest request, HttpServletResponse response) {
        log.info("进入易宝打款回调");
        StringBuffer strBuffer = new StringBuffer(2000);
        String backStr = "";
        try {
            request.setCharacterEncoding("GBK");
            BufferedReader reader = request.getReader();
            String temp = "";
            while ((temp = reader.readLine()) != null) {
                strBuffer.append(temp);
                if (strBuffer.length() > 500000) {
                    log.error("返回报文长度超过500000");
                    return ;
                }
            }
            backStr = strBuffer.toString();
        } catch (IOException e) {
            log.error("接收回调参数异常", e);
            return ;
        }
        log.info("易宝代付回调...参数:{}", backStr);
        
        Result<String> result = withdrawService.withdrawCallback(WithdrawChannel.YEEPAY_WITHDRAW, backStr);
        if(!result.isSuccess()) {
            return;
        }
        
        PrintWriter writer=null;
        try {
            response.setContentType("text/xml;charset=gbk");
            response.setCharacterEncoding("gbk");
            writer=response.getWriter();
            log.info("提现回调响应报文:{}", result.getObject());
            writer.write(result.getObject());
        } catch (Exception e) {
            log.error("yeepay withdrawCallback response error");
        }finally{
            if(writer!=null){
                writer.flush();
                writer.close();
            }
        }
    }
    
    /**
     * 宝付代付异步回调
     * @param request
     * @return
     */
    @RequestMapping(value = "/baofoo/withdrawNotify.do")
    @ResponseBody
    public String baofooWithdrawNotify(HttpServletRequest request, HttpServletResponse response) {
        log.info("进入宝付打款回调");
        StringBuilder strBuffer = new StringBuilder(2000);
        String backStr = "";
        try {
            request.setCharacterEncoding("utf-8");
            BufferedReader reader = request.getReader();
            String temp = "";
            while ((temp = reader.readLine()) != null) {
                strBuffer.append(temp);
                if (strBuffer.length() > 500000) {
                    log.error("返回报文长度超过500000");
                    return "";
                }
            }
            backStr = strBuffer.toString();
        } catch (IOException e) {
            log.error("接收回调参数异常", e);
            return "";
        }
        log.info("宝付代付回调...参数:{}", backStr);
        
        Result<String> result = withdrawService.withdrawCallback(WithdrawChannel.BAOFOO_WITHDRAW, backStr);
        if(!result.isSuccess()){
            return "";
        }
        
        return result.getObject();
    }
}
