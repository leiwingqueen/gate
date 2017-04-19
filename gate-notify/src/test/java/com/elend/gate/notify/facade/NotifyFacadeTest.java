package com.elend.gate.notify.facade;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.p2p.util.OrderIdHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:config/spring/*.xml" })
public class NotifyFacadeTest {
    @Autowired
    private NotifyFacade facade;
    @Test
    public void testAddQueue() {
        String orderId=OrderIdHelper.newOrderId();
        String partnerOrderId=OrderIdHelper.newOrderId();
        String params="a=1&b=3";
        String notifyUrl="http://127.0.0.1:8080";
        int retryTime=1;
        facade.addQueue(orderId, PartnerConstant.P2P, 
                        partnerOrderId, params, notifyUrl, new Date(),retryTime);
    }

}
