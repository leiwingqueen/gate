package com.elend.gate.conf.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时reload初始化数据
 * @author mgt
 *
 */
public class InitData {

    private static Logger logger = LoggerFactory.getLogger(InitData.class);
    
    public synchronized static void reload() {
        
        logger.info("CityData.init() start ...");
        CityData.init();
        logger.info("CityData.init() complete.");
        
        logger.info("BankPayLimitData.init() start ...");
        BankPayLimitData.init();
        logger.info("BankPayLimitData.init() complete.");
    }

}
