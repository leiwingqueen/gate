package com.elend.gate.channel;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elend.gate.channel.constant.ChannelIdConstant;
import com.elend.gate.channel.service.PayChannelService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:config/spring/*.xml" })
public class ChannelFactoryTest {
    @Test
    public void testGetPayChannel() {
        PayChannelService service=ChannelFactory.getPayChannel(ChannelIdConstant.YEEPAY);
        Assert.assertTrue(service!=null);
    }
}
