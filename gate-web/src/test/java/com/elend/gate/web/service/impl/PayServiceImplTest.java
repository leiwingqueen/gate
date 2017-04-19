package com.elend.gate.web.service.impl;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.gate.web.service.PayService;
import com.elend.p2p.Result;
import com.elend.p2p.gson.JSONUtils;
import com.google.gson.reflect.TypeToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:config/spring/*.xml" })
public class PayServiceImplTest {
    @Autowired
    private PayService payService;
    @Test
    public void testChargeRequest() {
        fail("Not yet implemented");
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
        Result<RequestFormData> result=payService.chargeCallback(ChannelIdConstant.LIANLIAN_MOBILE, params);
        TypeToken<Result<RequestFormData>> token=new TypeToken<Result<RequestFormData>>(){};
        System.out.println("result:"+JSONUtils.toJson(result, token.getType(), false));
        Assert.assertTrue(result.isSuccess());
    }
}
