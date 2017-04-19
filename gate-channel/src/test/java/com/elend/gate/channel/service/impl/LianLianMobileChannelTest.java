package com.elend.gate.channel.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;




import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.elend.gate.channel.PartnerResult;
import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.constant.PayType;
import com.elend.gate.channel.facade.vo.AuthPartnerChargeData;
import com.elend.gate.channel.facade.vo.ChargeCallbackData;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.gson.JSONUtils;
import com.elend.p2p.util.OrderIdHelper;
import com.google.gson.reflect.TypeToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:config/spring/*.xml" })
public class LianLianMobileChannelTest {
    @Autowired
    private LianLianMobileChannel channel;
    @Test
    @Transactional
    public void testCharge() {
        AuthPartnerChargeData authChargeData=new AuthPartnerChargeData();
        authChargeData.setAmount(new BigDecimal("100"));
        authChargeData.setUserId(100L);
        authChargeData.setRealName("张三");
        authChargeData.setIdCard("442000198704051110");
        authChargeData.setRegisterTime(new Date());
        RequestFormData formData=channel.charge(OrderIdHelper.newOrderId(), authChargeData);
        System.out.println("formData:"+JSONUtils.toJson(formData, false));
    }

    @Test
    public void testChargeCallback() {
        Map<String, String> params=new HashMap<String, String>();
        params.put("acct_name", "赖睿锐");
        params.put("bank_code", "01020000");
        params.put("dt_order", "20150806170133");
        params.put("id_no", "362128198309260057");
        params.put("id_type", "0");
        params.put("money_order", "0.01");
        params.put("no_agree", "2015080617029648");
        params.put("no_order", "15080617013300253001");
        params.put("oid_partner", "201408071000001543");
        params.put("oid_paybill", "2015080610279493");
        params.put("pay_type", "D");
        params.put("result_pay", "SUCCESS");
        params.put("settle_date", "20150806");
        params.put("sign", "2aac5c9e9af142e5e66f2db265ea86c2");
        params.put("sign_type", "MD5");
        
        PartnerResult<ChargeCallbackData> result=channel.chargeCallback(ChannelIdConstant.LIANLIAN_MOBILE, params);
        TypeToken<PartnerResult<ChargeCallbackData>> token=new TypeToken<PartnerResult<ChargeCallbackData>>(){};
        System.out.println("result:"+JSONUtils.toJson(result, token.getType(), false));
        Assert.assertTrue(result.isSuccess());
    }
    @Test
    public void testFeeCal(){
        BigDecimal fee=channel.feeCalculate(new BigDecimal("300"), PayType.PERSON);
        System.out.println("fee:"+fee);
    }
}
