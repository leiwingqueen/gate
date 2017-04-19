package com.elend.gate.channel.facade;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.elend.gate.channel.PartnerResult;
import com.elend.gate.channel.constant.BankIdConstant;
import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.facade.vo.AuthPartnerChargeData;
import com.elend.gate.channel.facade.vo.PartnerChargeData;
import com.elend.gate.channel.facade.vo.PartnerSingleQueryChargeData;
import com.elend.gate.util.vo.RequestFormData;
import com.elend.p2p.util.OrderIdHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:config/spring/*.xml" })
public class PayChannelFacadeTest {
    @Autowired
    private PayChannelFacade facade;
    @Test
    @Transactional
    public void testCharge() {
        PartnerChargeData chargeData=new PartnerChargeData();
        chargeData.setAmount(new BigDecimal(1));
        chargeData.setBankId(BankIdConstant.PINGAN);
        RequestFormData form=facade.charge(ChannelIdConstant.YEEPAY,"1",chargeData);
        System.out.println("form:"+form);
    }
    
    @Test
    @Transactional
    public void testAuthCharge() {
        AuthPartnerChargeData chargeData=new AuthPartnerChargeData();
        chargeData.setAmount(new BigDecimal(1));
        chargeData.setRealName("真实姓名");
        chargeData.setBankAccount("卡号");
        chargeData.setIdCard("身份证");
        RequestFormData form=facade.charge(ChannelIdConstant.LIANLIAN_MOBILE,OrderIdHelper.newOrderId(),chargeData);
        System.out.println("form:"+form);
    }
    
    @Test
    public void testSingleQueryCharge() {
        //PartnerResult<PartnerSingleQueryChargeData> rs = facade.singleQueryCharge(ChannelIdConstant.LIANLIAN_MOBILE, "15073014201504020034");
        PartnerResult<PartnerSingleQueryChargeData> rs = facade.singleQueryCharge(ChannelIdConstant.BAOFOO_GATE, "160823094229230010620001");
        //PartnerResult<PartnerSingleQueryChargeData> rs = facade.singleQueryCharge(ChannelIdConstant.LIANLIAN_MOBILE, "15073014201504020034");
        System.out.println("rs:" + rs);
    }
}
