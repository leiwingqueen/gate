package com.elend.gate.util.vo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 表单请求数据
 * @author liyongquan 2015年5月21日
 *
 */
public class RequestFormData {
    /**map初始化大小*/
    private static final int DEFAULT_INITIAL_CAPACITY=32;
    /**请求参数*/
    private Map<String,String> params=new HashMap<String, String>(DEFAULT_INITIAL_CAPACITY);
    /**表单跳转地址*/
    private String requestUrl;
    /**
     * 是否点对点通知
     */
    private boolean isNotify=false;
    public Map<String, String> getParams() {
        return params;
    }
    public void setParams(Map<String, String> params) {
        this.params = params;
    }
    public String getRequestUrl() {
        return requestUrl;
    }
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }
    /**
     * 增加参数
     * @param key
     * @param value
     */
    public void addParam(String key,String value){
        if(StringUtils.isBlank(value))value="";
        params.put(key, value);
    }
    
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    public boolean isNotify() {
        return isNotify;
    }
    public void setNotify(boolean isNotify) {
        this.isNotify = isNotify;
    }
}
