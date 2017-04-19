package com.elend.gate.conf.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 系统配置
 * @author liyongquan 2015年6月17日
 *
 */
@Component
public class SystemConfig {
    @Autowired
    private PropertyFacade facade;
    /**
     * 测试标志位
     * @return
     */
    public boolean isTest(){
        return facade.getBoolean("test_flag", false);
    }
    
    /**
     * 测试标志位
     * @return
     */
    public boolean isUmbpayChargeTest(){
        return facade.getBoolean("umbpay_charge_test_flag", false);
    }
}
