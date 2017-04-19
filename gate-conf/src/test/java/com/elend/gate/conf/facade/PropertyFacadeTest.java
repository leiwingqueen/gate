package com.elend.gate.conf.facade;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:config/spring/*.xml" })
public class PropertyFacadeTest {
    @Autowired
    private PropertyFacade facade;
    @Test
    public void testGetProperty() {
        //先等待5S，避免还没加载到内存
        try {
            Thread.sleep(5*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String s=facade.getProperty("lianlian_mobile_merId", "");
        System.out.println("yeepay_merId:"+s);
    }
}
