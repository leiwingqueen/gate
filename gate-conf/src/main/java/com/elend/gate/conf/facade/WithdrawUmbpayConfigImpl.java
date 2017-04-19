package com.elend.gate.conf.facade;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.conf.constant.ChannelConfigConstant;
import com.elend.gate.conf.constant.GateConstant;
import com.elend.p2p.util.encrypt.DesPropertiesEncoder;

@Component
public class WithdrawUmbpayConfigImpl extends WithdrawUmbpayConfig{
    @Autowired
    private PropertyFacade facade;
    @Autowired
    private DesPropertiesEncoder encoder;
    @Autowired
    private GateConstant gateConstant;
    //缓存商户ID和私钥，如果值发生改变才重新做解密
    private Map<String,String> encriptCache = new HashMap<String, String>();
    private Map<String,String> decriptCache = new HashMap<String, String>();
    
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
        encriptCache.put(key, decript);
        return decript;
    }

    /**
     * 获取数据库中完整的key
     * @param key
     * @return
     */
    private String getFullKey(String key){
        return ChannelConfigConstant.UMBPAY_WITHDRAW + key;
    }
    
    @Override
    public String getMchtNo() {
        return getEncriptKey("mcht_no");
    }
    
    @Override
    public String getPwd() {
        return getEncriptKey("pwd");
    }
    
    @Override
    public String getRequestUril() {
        return facade.getProperty(getFullKey("request_url"));
    }
    
    @Override
    public String getFixFee() {
        return facade.getProperty(getFullKey("fee"));
    }

}
