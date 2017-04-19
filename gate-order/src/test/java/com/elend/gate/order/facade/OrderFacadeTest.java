package com.elend.gate.order.facade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elend.gate.balance.constant.SubSubject;
import com.elend.gate.balance.constant.Subject;
import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.constant.PartnerResultCode;
import com.elend.gate.channel.constant.PayType;
import com.elend.gate.channel.constant.WithdrawChannel;
import com.elend.gate.channel.constant.WithdrawStatus;
import com.elend.gate.channel.facade.vo.ChargeCallbackData;
import com.elend.gate.channel.facade.vo.WithdrawCallbackData;
import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.gate.order.constant.RequestSource;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.Result;
import com.elend.p2p.util.OrderIdHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:config/spring/*.xml" })
public class OrderFacadeTest {
    @Autowired
    private OrderFacade facade;
    @Test
    public void testSendChargeRequest() {
        ChannelIdConstant channelId=ChannelIdConstant.YEEPAY;
        PartnerConstant partnerId=PartnerConstant.P2P;
        String partnerOrderId=OrderIdHelper.newOrderId();
        String orderId=OrderIdHelper.newOrderId();
        BigDecimal amount=new BigDecimal(100);
        RequestFormData form=new RequestFormData();
        form.setRequestUrl("http://test");
        String ip="127.0.0.1";
        RequestSource source=RequestSource.WEB;
        Result<String> result=facade.sendChargeRequest(channelId, partnerId,
                                                       partnerOrderId, amount, form, ip, source,
                                                       "","",orderId, PayType.PERSON);
        Assert.assertTrue(result.isSuccess());
    }
    
    @Test
    public void testChargeCallback(){
        boolean success=true;
        ChannelIdConstant channelId=ChannelIdConstant.YEEPAY;
        ChargeCallbackData callbackData=new ChargeCallbackData();
        callbackData.setAmount(new BigDecimal(101));
        callbackData.setCallbackStr("SUCCESS");
        callbackData.setChannelBankId("ICBC");
        String channelOrderId=OrderIdHelper.newOrderId();
        callbackData.setChannelOrderId(channelOrderId);
        Date now=new Date();
        callbackData.setNoticeTime(now);
        callbackData.setNotify(false);
        callbackData.setOrderId("15070117592704056079");
        callbackData.setPayTime(now);
        callbackData.setFee(new BigDecimal(2));
        Result<String> result=facade.chargeCallback(PartnerResultCode.SUCCESS, channelId, callbackData,new HashMap<String, String>());
        Assert.assertTrue(result.isSuccess());
    }
    
    @Test
    public void testWithsrawCallback(){
        boolean success = true;
        
        WithdrawChannel channel = WithdrawChannel.YEEPAY_WITHDRAW;
        
        WithdrawCallbackData backData = new WithdrawCallbackData();
        
        backData.setAmount(BigDecimal.TEN);
        backData.setCallbackStr("back");
        backData.setCallbackTime(new Date());
        backData.setChannelOrderId("123456789");
        backData.setFee(new BigDecimal(2));
        backData.setMessage("成功");
        backData.setNotify(false);
        backData.setOrderId("15111315402631912");
        backData.setRequestUrl("http://32323");
        backData.setResponseStr("232323");
        backData.setResultStr("32323");
        backData.setWithdrawStatus(WithdrawStatus.SUCCESS);
        
        Result<String> result=facade.withdrawCallback(success, channel, backData);
        Assert.assertTrue(result.isSuccess());
    }
    
    @Test
    public void testTransfer(){
        boolean success = true;
        
        WithdrawChannel channel = WithdrawChannel.YEEPAY_WITHDRAW;
        
        WithdrawCallbackData backData = new WithdrawCallbackData();
        
        backData.setAmount(BigDecimal.TEN);
        backData.setCallbackStr("back");
        backData.setCallbackTime(new Date());
        backData.setChannelOrderId("123456789");
        backData.setFee(new BigDecimal(2));
        backData.setMessage("成功");
        backData.setNotify(false);
        backData.setOrderId("15111315402631912");
        backData.setRequestUrl("http://32323");
        backData.setResponseStr("232323");
        backData.setResultStr("32323");
        backData.setWithdrawStatus(WithdrawStatus.SUCCESS);
        
        Result<String> result = facade.transfer(200, 300, new BigDecimal(200), Subject.TRANSFER_BANK, SubSubject.TRANSFER_BANK, "提现到银行卡");
        Assert.assertTrue(result.isSuccess());
    }
}
