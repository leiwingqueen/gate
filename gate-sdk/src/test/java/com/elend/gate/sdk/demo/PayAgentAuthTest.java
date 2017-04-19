package com.elend.gate.sdk.demo;

import java.math.BigDecimal;

import org.junit.Test;

import com.elend.gate.sdk.BankAccountType;
import com.elend.gate.sdk.GateHelper;
import com.elend.gate.sdk.PayAgentChannel;
import com.elend.gate.sdk.vo.PartnerPayAgentChargeResponse;
import com.elend.gate.sdk.vo.PartnerPayAgentCheckCodeResponse;
import com.elend.gate.sdk.vo.PartnerPayAgentGetCodeResponse;
import com.elend.gate.sdk.vo.PayAgentChargeResponse;
import com.elend.gate.sdk.vo.PayAgentCheckCodeResponse;
import com.elend.gate.sdk.vo.PayAgentGetCodeResponse;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.Result;
import com.elend.p2p.gson.JSONUtils;
import com.elend.p2p.util.HttpUtils;
import com.google.gson.reflect.TypeToken;

/**
 * 代扣鉴权
 * @author mgt
 *
 */
public class PayAgentAuthTest {

    @Test
    public void testGetCode() {
        RequestFormData form = GateHelper.payAgentGetCodeRequest(
                                                                 "P2P", 
                                                                 "1234567891078", 
                                                                 new BigDecimal("5"), 
                                                                 PayAgentChannel.UMBPAY_PAY_AGENT, 
                                                                 "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl", 
                                                                 "http://test.gate.gzdai.com/payAgent/getCode.do", 
                                                                 "127.0.0.1", 
                                                                 "102", 
                                                                 "银行卡号", 
                                                                 "真实姓名", 
                                                                 "身份证号", 
                                                                 "手机号码"
                                                                );
        String s = HttpUtils.doPost(form.getRequestUrl(), form.getParams());
        System.out.println("==============> s:" + s);
        
        Result<PartnerPayAgentGetCodeResponse> result = JSONUtils.fromJson(s, new TypeToken<Result<PartnerPayAgentGetCodeResponse>>() {});
        System.out.println("result:" + result);
        
        PayAgentGetCodeResponse response = GateHelper.getPayAgentGetCodeResponse(result.getObject(), "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl");
        
        System.out.println("=====================>" + response);
    }
    
    
    @Test
    public void testCheckCode() {
        RequestFormData form = GateHelper.payAgentCheckCodeRequest(
                                                                   "P2P", 
                                                                   "223456789106841", 
                                                                   "1234567891078",
                                                                   "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl", 
                                                                   "http://test.gate.gzdai.com/payAgent/checkCode.do", 
                                                                   "127.0.0.1", 
                                                                   "123456", 
                                                                   "http://127.0.0.1:5678/gate/payAgentAuthCallback.do"
                                                                   );
        
        String s = HttpUtils.doPost(form.getRequestUrl(), form.getParams());
        System.out.println("==============> s:" + s);
        
        Result<PartnerPayAgentCheckCodeResponse> result = JSONUtils.fromJson(s, new TypeToken<Result<PartnerPayAgentCheckCodeResponse>>() {});
        System.out.println("result:" + result);
        
        PayAgentCheckCodeResponse response = GateHelper.getPayAgentCheckCodeResponse(result.getObject(), "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl");
        
        System.out.println("=====================>" + response);
    }
    
    @Test
    public void testCharge() {
        RequestFormData form = GateHelper.payAgentChargeRequest(
                                                                "P2P", 
                                                                "14785237454354646", 
                                                                "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl",
                                                                "http://127.0.0.1:8080/payAgent/charge.do", 
                                                                "127.0.0.1", 
                                                                "102", 
                                                                "银行卡号", 
                                                                "真实姓名", 
                                                                "身份证号", 
                                                                "银行预留手机", 
                                                                "100000", 
                                                                "http://127.0.0.1:5678/gate/payAgentChargeCallback.do",
                                                                PayAgentChannel.UMBPAY_PAY_AGENT,
                                                                BankAccountType.PRIVATE);
        
        String s = HttpUtils.doPost(form.getRequestUrl(), form.getParams());
        System.out.println("==============> s:" + s);
        
        Result<PartnerPayAgentChargeResponse> result = JSONUtils.fromJson(s, new TypeToken<Result<PartnerPayAgentChargeResponse>>() {});
        System.out.println("result:" + result);
        
        PayAgentChargeResponse response = GateHelper.getPayAgentChargeResponse(result.getObject(), "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl");
        
        System.out.println("=====================>" + response);
    }
    
    public static void main(String[] args) {
        for(int i = 10; i < 24*60*60; ) {
            double buf = Math.pow(i, 2) / 100;
            i = (int) (i + buf);
            System.out.println(i);
        }
    }
}
