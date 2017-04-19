package com.elend.gate.conf.facade;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.conf.constant.ChannelConfigConstant;
import com.elend.gate.conf.constant.GateConstant;
import com.elend.p2p.util.encrypt.DesPropertiesEncoder;

@Component
public class WithdrawYeepayConfigImpl extends WithdrawYeepayConfig{
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
        return ChannelConfigConstant.YEEPAY_WITHDRAW + key;
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
        String fee = facade.getProperty(getFullKey("fee"));
        return fee;
    }

    @Override
    public String getSecureCertificateName() {
        return facade.getProperty(getFullKey("secure_certificate_name"));
    }

    @Override
    public String getPfxPwd() {
        return getEncriptKey("pfx_pwd");
    }

    @Override
    public String getQueryBalanceUrl() {
        return facade.getProperty(getFullKey("balance_url"));
    }

}
