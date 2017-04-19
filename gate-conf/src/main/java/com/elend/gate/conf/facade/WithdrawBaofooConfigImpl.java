package com.elend.gate.conf.facade;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.conf.constant.ChannelConfigConstant;
import com.elend.gate.conf.constant.GateConstant;
import com.elend.p2p.util.encrypt.DesPropertiesEncoder;

@Component
public class WithdrawBaofooConfigImpl extends WithdrawBaofooConfig {
    
    @Autowired
    private PropertyFacade facade;
    @Autowired
    private DesPropertiesEncoder encoder;
    @Autowired
    private GateConstant gateConstant;
    
    @Autowired
    private BaofooGateConfig baofooGateConfig;
    
    //缓存商户ID和私钥，如果值发生改变才重新做解密
    private Map<String,String> encriptCache = new HashMap<String, String>();
    private Map<String,String> decriptCache = new HashMap<String, String>();
    
    /**
     * 获取加密的配置
     * @param key
     * @return
     */
    private String getEncriptKey(String key, String defaultValue){
        String enc=facade.getProperty(getFullKey(key), defaultValue);
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
        return ChannelConfigConstant.BAOFOO_WITHDRAW + key;
    }
    
    @Override
    public String getMchtNo() {
        //return facade.getProperty(getFullKey("mcht_no"), "100000178");
        return baofooGateConfig.getMerId();
    }
    
    @Override
    public String getPwd() {
        return getEncriptKey("pwd", "3384381E1FBB173C");
    }
    
    @Override
    public String getRequestUril() {
        return facade.getProperty(getFullKey("request_url"), "https://public.baofoo.com/baofoo-fopay/pay/BF0040001.do");
    }
    
    @Override
    public String getQueryRequestUril() {
        return facade.getProperty(getFullKey("query_request_url"), "https://public.baofoo.com/baofoo-fopay/pay/BF0040002.do");
    }
    
    @Override
    public String getFixFee() {
        return facade.getProperty(getFullKey("fee"), "2.00");
    }

    @Override
    public String getTerminalId() {
        return facade.getProperty(getFullKey("terminal_id"), "100000859");
    }

    @Override
    public String getPrivateKeyFile() {
        return facade.getProperty(getFullKey("private_key_file"), "/data/app/gate-web/config/baofoo/m_pri.pfx");
    }

    @Override
    public String getPublicKeyFile() {
        return facade.getProperty(getFullKey("public_key_file"), "/data/app/gate-web/config/baofoo/baofoo_pub.cer");
    }

}
