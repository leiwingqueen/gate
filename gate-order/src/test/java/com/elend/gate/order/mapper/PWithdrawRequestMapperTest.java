package com.elend.gate.order.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:config/spring/*.xml" })
public class PWithdrawRequestMapperTest {
    
    @Autowired
    private PWithdrawRequestMapper pWithdrawRequestMapper;

    @Test
    public void testUpdateChannelOrderId() {
        pWithdrawRequestMapper.updateChannelOrderId("160519155405710", "123456");
    }

}
