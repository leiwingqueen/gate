package com.elend.gate.conf.facade;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.conf.constant.ChannelConfigConstant;
import com.elend.gate.conf.constant.GateConstant;
import com.elend.p2p.util.encrypt.DesPropertiesEncoder;

/**
 * 连连移动端SDK配置
 * @author liyongquan 2015年7月15日
 *
 */
@Component
public class LianlianMobileConfig{
    @Autowired
    private PropertyFacade facade;
    @Autowired
    private DesPropertiesEncoder encoder;
    @Autowired
    private GateConstant gateConstant;
    //缓存商户ID和私钥，如果值发生改变才重新做解密
    private Map<String,String> encriptCache=new HashMap<String, String>();
    private Map<String,String> decriptCache=new HashMap<String, String>();
    /**
     * 商户ID
     * @return
     */
    public String getMerId(){
        return facade.getProperty(getFullKey("merId"));
    }
    /**
     * 获取MD5方式加密KEY
     * @return
     */
    public String getPriKey(){
        return getEncriptKey("priKey");
    }
    
    /**
     * 获取加密的配置
     * @param key
     * @return
     */
    private String getEncriptKey(String key){
        String enc=facade.getProperty(getFullKey(key));
        //加密的值不变，直接返回缓存的解密值
        if(enc.equals(encriptCache.get(key))){
            return decriptCache.get(key);
        }
        //更新缓存
        String decript=encoder.decode(enc);
        encriptCache.put(key, enc);
        decriptCache.put(key, decript);
        return decript;
    }
    /**
     * 获取点对点通知url
     * @return
     */
    public String getNotifyUrl() {
        return String.format(facade.getProperty(getFullKey("notifyUrl")),
                             gateConstant.getDomain());
    }
    
    /**
     * 获取数据库中完整的key
     * @param key
     * @return
     */
    private String getFullKey(String key){
        return ChannelConfigConstant.LIANLIAN_MOBILE+key;
    }
    
    public String getQueryRefundReqURL() {
    	return facade.getProperty(getFullKey("queryRefundReqURL"));
    }
    
    /**
     * 充值费率
     * @return
     */
    public BigDecimal getFeePercentage(){
        return new BigDecimal(facade.getProperty(getFullKey("feePercentage"),"0.0025"));
    }
}
