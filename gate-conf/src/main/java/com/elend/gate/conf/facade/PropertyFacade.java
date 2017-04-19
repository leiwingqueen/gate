package com.elend.gate.conf.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.conf.service.PropertyService;
/**
 * 通用的属性配置模块
 * @author liyongquan 2015年5月27日
 *
 */
@Component
public class PropertyFacade {
    @Autowired
    private PropertyService service;
    
    /**
     * 获取属性值
     * @param key--键
     * @return
     */
    public String getProperty(String key){
        return service.getProperty(key, "");
    }
    /**
     * 获取属性值
     * @param key--键
     * @param defaultValue--默认值
     * @return
     */
    public String getProperty(String key,String defaultValue){
        return service.getProperty(key, defaultValue);
    }
    public int getInt(String key,int defaultValue){
         String value=service.getProperty(key, defaultValue+"");
         try{
             return Integer.parseInt(value);
         }catch(NumberFormatException e){
             return defaultValue;
         }
    }
    
    public int getInt(String key){
        return getInt(key,0);
    }
    
    public boolean getBoolean(String key,boolean defaultValue){
        String value=service.getProperty(key, defaultValue+"");
        return Boolean.parseBoolean(value);
    }
    
    public boolean getBoolean(String key){
        String value=service.getProperty(key,"false");
        return Boolean.parseBoolean(value);
    }
}
