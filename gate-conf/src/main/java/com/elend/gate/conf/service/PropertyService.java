package com.elend.gate.conf.service;

/**
 * 属性配置
 * @author liyongquan
 *
 */
public interface PropertyService {
    /**
     * 获取属性值
     * @param key--键
     * @param defaultValue--默认值
     * @return
     */
    String getProperty(String key,String defaultValue);
}
