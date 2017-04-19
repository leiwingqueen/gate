package com.elend.gate.conf.facade;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:config/spring/*.xml" })
public class YeepayConfigTest {
    public YeepayConfigTest(){
        //先等待5S，避免还没加载到内存
        try {
            Thread.sleep(5*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Autowired
    private YeepayConfig config;
    @Test
    public void testGetMerId() {
        System.out.println("merId:"+config.getMerId());
    }
    
    @Test
    public void testGetCallback(){
        System.out.println("callbackURL:"+config.getCallbackUrl());
    }
    
    @Test
    public void testPriKey() {
        System.out.println("priKey:"+config.getPriKey());
        System.out.println(config.getCallbackUrl());
    }

}
