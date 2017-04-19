package com.elend.gate.conf.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.elend.gate.conf.mapper.SSystemPropertiesMapper;
import com.elend.gate.conf.model.SSystemPropertiesPO;
import com.elend.gate.conf.service.PropertyService;

public class PropertyServiceImpl extends Thread implements PropertyService{
    private Logger logger = LoggerFactory.getLogger(PropertyServiceImpl.class);
    /**
     * map初始化大小
     */
    private static final int INIT_SIZE=50; 
    /**
     * 线程是否在工作中
     */
    private boolean working;

    /** RELOAD间隔：90s */
    private int interval = 90000;
    
    private Map<String,String> properties=new HashMap<String, String>(INIT_SIZE);
    @Autowired
    private SSystemPropertiesMapper mapper;
    
    @Override
    public String getProperty(String key, String defaultValue) {
        if(properties==null||properties.size()==0){
            reload();
        }
        if(!properties.containsKey(key)){
            return defaultValue;
        }
        return properties.get(key);
    }
    
    /**
     * 增加键值
     * @param key
     * @param value
     */
    private void addProperty(String key,String value){
        if(StringUtils.isBlank(key)){
            return;
        }
        if(StringUtils.isBlank(value))value="";
        properties.put(key, value);
    }
    /**
     * 重新加载配置
     */
    private void reload(){
        List<SSystemPropertiesPO> list=mapper.listAll();
        if(list!=null&&list.size()>0){
            for(SSystemPropertiesPO po:list){
                addProperty(po.getPropertyKey(), po.getPropertyValue());
            }
        }
    }
    
    @Override
    public void run(){
        working = true;
        logger.info(super.getName() + " started ...");
        while (working) {
            try {
                long t1 = System.currentTimeMillis();
                // reload
                reload();
                long time = System.currentTimeMillis() - t1;
                logger.info("Data reloaded successfully, time=" + time + " ms");
            } catch (Exception e) {
                logger.error("Data reloaded failed: " + e.getMessage(), e);
            }
            try {
                // sleep在一个独立的try{} catch{}块里，即使上面程序总是出错，也不会造成死循环。
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                logger.info(super.getName() + " was interrupted.");
            }
        }
        logger.info(super.getName() + " stopped ...");
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
    
    public void stopWorking(){
        this.working=false;
    }
}
