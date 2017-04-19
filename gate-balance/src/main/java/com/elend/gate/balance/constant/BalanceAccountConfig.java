package com.elend.gate.balance.constant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.conf.facade.PropertyFacade;

/**
 * 余额账户配置
 * @author mgt
 */
@Component
public class BalanceAccountConfig{
    @Autowired
    private PropertyFacade facade;
}
