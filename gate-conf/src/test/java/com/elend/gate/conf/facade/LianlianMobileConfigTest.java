package com.elend.gate.conf.facade;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:config/spring/*.xml" })
public class LianlianMobileConfigTest {
    @Autowired
    private LianlianMobileConfig config;
    @Test
    public void testGetMerId() {
        System.out.println("merId:"+config.getMerId());
    }

    @Test
    public void testGetPriKey() {
        System.out.println("prikey:"+config.getPriKey());
    }

    @Test
    public void testGetNotifyUrl() {
        System.out.println("notifyUrl:"+config.getNotifyUrl());
    }

}
