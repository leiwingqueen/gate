package com.elend.gate.channel.facade;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elend.gate.channel.PartnerResult;
import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.constant.PartnerResultCode;
import com.elend.gate.channel.constant.WithdrawChannel;
import com.elend.gate.channel.facade.vo.ChargeCallbackData;
import com.elend.gate.channel.facade.vo.PartnerChargeData;
import com.elend.gate.channel.facade.vo.PartnerChargeRequest;
import com.elend.gate.channel.facade.vo.PartnerWithdrawData;
import com.elend.gate.channel.facade.vo.PartnerWithdrawRequest;
import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.gate.conf.facade.PartnerConfig;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.gson.JSONUtils;
import com.elend.p2p.util.OrderIdHelper;
import com.yeepay.DigestUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:config/spring/*.xml" })
public class PartnerFacadeTest {
    @Autowired
    private PartnerFacade facade;
    @Autowired
    private PartnerConfig config;
    @Test
    public void testChargeRequest() throws IllegalArgumentException, IllegalAccessException {
        /**
         * 构造请求参数
         */
        String partnerId="P2P";
        String partnerOrderId=OrderIdHelper.newOrderId();
        String amount="5.2121";
        String redirectUrl="http://test.gate.gzdai.com";
        String notifyUrl="http://127.0.0.1:8080";
        String payChannel="YEEPAY";
        String timeStamp=new Date().getTime()+"";
        //String ip="127.0.0.1";
        String ip="125.94.36.29";
        
        StringBuffer sValue = new StringBuffer();
        sValue.append(partnerId);
        sValue.append(partnerOrderId);
        sValue.append(amount);
        sValue.append(payChannel);
        sValue.append(redirectUrl);
        sValue.append(notifyUrl);
        sValue.append(timeStamp);
        sValue.append(ip);
        String hmac= DigestUtil.hmacSign(sValue.toString(),config.getPriKey(PartnerConstant.P2P));
        PartnerChargeRequest params=new PartnerChargeRequest();
        params.setHmac(hmac);
        params.setPartnerId(partnerId);
        params.setPartnerOrderId(partnerOrderId);
        params.setAmount(amount);
        params.setPayChannel(payChannel);
        params.setRedirectUrl(redirectUrl);
        params.setNotifyUrl(notifyUrl);
        params.setBankId("307");
        params.setTimeStamp(timeStamp);
        params.setIp(ip);
        printParams(params);
        PartnerChargeData chargeData=facade.chargeRequest(params);
        System.out.println(JSONUtils.toJson(chargeData,false));
    }
    /**
     * 打印参数,方便测试
     * @param params--参数
     * @param c--类
     * @param buffer--字符串缓冲区
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    @SuppressWarnings("rawtypes")
    private void printParams(Object params,Class c,StringBuffer buffer) throws IllegalArgumentException, IllegalAccessException{
        if(c==null)return;
        for(Field field:c.getDeclaredFields()){
            field.setAccessible(true);
            String value=field.get(params)==null?"":field.get(params).toString();
            if(buffer.length()!=0){
                buffer.append("&");
            }
            buffer.append(field.getName()+"="+value);
        }
        //打印父类的属性
        printParams(params, c.getSuperclass(), buffer);
    }
    
    /**
     * 打印参数,方便测试
     * @param params
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    private void printParams(Object params) throws IllegalArgumentException, IllegalAccessException{
        StringBuffer buffer=new StringBuffer(200);
        this.printParams(params, params.getClass(), buffer);
        System.out.println(buffer.toString());
    }

    @Test
    public void testGetChargeCallbackForm() {
        String partnerOrderId=OrderIdHelper.newOrderId();
        String redirectUrl="http://127.0.0.1:8080";
        ChargeCallbackData callbackData=new ChargeCallbackData();
        callbackData.setAmount(new BigDecimal("0.01"));
        callbackData.setCallbackStr("SUCCESS");
        callbackData.setChannelBankId("");
        callbackData.setChannelOrderId(OrderIdHelper.newOrderId());
        Date now=new Date();
        callbackData.setNoticeTime(now);
        callbackData.setNotify(true);
        callbackData.setOrderId(OrderIdHelper.newOrderId());
        callbackData.setPayTime(now);
        PartnerResult<ChargeCallbackData> result=new PartnerResult<ChargeCallbackData>(PartnerResultCode.SUCCESS,callbackData);
        RequestFormData form=facade.getChargeCallbackForm(ChannelIdConstant.YEEPAY,
                                     PartnerConstant.P2P, partnerOrderId, result, redirectUrl);
        System.out.println("form:"+JSONUtils.toJson(form,false));
    }
    
    @Test
    public void testWithdrawRequest()throws IllegalArgumentException, IllegalAccessException{
        /**
         * 构造请求参数
         */
        String partnerId="P2P";
        String partnerOrderId=OrderIdHelper.newOrderId();
        String amount="0.01";
        String notifyUrl="http://127.0.0.1:8080";
        String payChannel=WithdrawChannel.UMBPAY_WITHDRAW.name();
        String timeStamp=new Date().getTime()+"";
        //String ip="127.0.0.1";
        String ip="125.94.36.29";
        String bankId="307";
        String bankAccount="1231123";
        String userName="李泳权";
        String bankCityId="1";
        String bankProvinceId="2";
        String bankBranchName="bankBranchName";
        String accountType="2";
        StringBuffer sValue = new StringBuffer();
        sValue.append(partnerId);
        sValue.append(partnerOrderId);
        sValue.append(amount);
        sValue.append(payChannel);
        sValue.append(timeStamp);
        sValue.append(ip);
        sValue.append(notifyUrl);
        sValue.append(bankId);
        sValue.append(bankAccount);
        sValue.append(userName);
        sValue.append(bankCityId);
        sValue.append(bankProvinceId);
        sValue.append(bankBranchName);
        sValue.append(accountType);
        String hmac= DigestUtil.hmacSign(sValue.toString(),config.getPriKey(PartnerConstant.P2P));
        PartnerWithdrawRequest params=new PartnerWithdrawRequest();
        params.setHmac(hmac);
        params.setPartnerId(partnerId);
        params.setPartnerOrderId(partnerOrderId);
        params.setAmount(amount);
        params.setChannel(payChannel);
        params.setTimeStamp(timeStamp);
        params.setIp(ip);
        params.setNotifyUrl(notifyUrl);
        params.setBankId(bankId);
        
        params.setBankAccount(bankAccount);
        params.setUserName(userName);
        params.setBankCityId(bankCityId);
        params.setBankProvinceId(bankProvinceId);
        params.setBankBranchName(bankBranchName);
        params.setAccountType(accountType);
        printParams(params);
        PartnerWithdrawData withdrawData=facade.withdrawRequest(params);
        System.out.println(JSONUtils.toJson(withdrawData,false));
    }

}
