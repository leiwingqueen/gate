package com.elend.gate.conf.facade;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.p2p.util.encrypt.DesPropertiesEncoder;

/**
 * 获取接入方配置
 * @author liyongquan
 *
 */
@Component
public class PartnerConfigImpl implements PartnerConfig{
    @Autowired
    private PropertyFacade facade;
    @Autowired
    private DesPropertiesEncoder encoder;
    //缓存商户ID和私钥，如果值发生改变才重新做解密
    private Map<String,String> encriptCache=new HashMap<String, String>();
    private Map<String,String> decriptCache=new HashMap<String, String>();
    @Override
    public String getPriKey(PartnerConstant partnerId) {
        return getEncriptKey(partnerId,"priKey");
    }
    
    /**
     * 获取加密的配置
     * @param key
     * @return
     */
    private String getEncriptKey(PartnerConstant partnerId,String key){
        String fullKey=getFullKey(partnerId,key);
        String enc=facade.getProperty(fullKey);
        //加密的值不变，直接返回缓存的解密值
        if(enc.equals(encriptCache.get(fullKey))){
            return decriptCache.get(fullKey);
        }
        //更新缓存
        String decript=encoder.decode(enc);
        encriptCache.put(fullKey, enc);
        encriptCache.put(fullKey, decript);
        return decript;
    }
    
    /**
     * 获取数据库中完整的key
     * @param partnerId
     * @param key
     * @return
     */
    private String getFullKey(PartnerConstant partnerId,String key){
        return partnerId.name()+"_"+key;
    }

}
