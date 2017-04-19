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
 * 宝付网银支付配置
 * @author mgt
 * @date 2016年8月22日
 */
@Component
public class BaofooGateConfig {
    
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
     * 商品名称
     */
    public static final String PRODUCT_NAME = "广州易贷充值";
    
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
        decriptCache.put(key, decript);
        
        return decript;
    }
    
    /**
     * 商户ID
     * @return
     */
    public String getMerId(){
        return facade.getProperty(getFullKey("merId"), "100000178");
    }
    
    /**
     * 获取MD5方式加密KEY
     * @return
     */
    public String getPriKey(){
        return getEncriptKey("priKey", "8D8AA64B5C64B35E");
    }
    
    public String getTerminalID(){
        return facade.getProperty(getFullKey("terminalId"), "10000001");
    }
    
    /**
     * 获取点对点通知url
     * @return
     */
    public String getNotifyUrl() {
        return String.format(facade.getProperty(getFullKey("notifyUrl"), "https://%s/baofooGate/notify.do"), gateConstant.getDomain());
    }
    
    /**
     * 同步返回地址
     * @return
     */
    public String getReturnUrl() {
        return String.format(facade.getProperty(getFullKey("returnUrl"), "https://%s/baofooGate/callback.jspx"), gateConstant.getDomain());
    }
    
    /**
     * 充值的订单查询地址
     * @return
     */
    public String getQueryUrl() {
        return facade.getProperty(getFullKey("queryUrl"), "https://gw.baofoo.com/order/query");
    }
    
    /**
     * 获取数据库中完整的key
     * @param key
     * @return
     */
    private String getFullKey(String key){
        return ChannelConfigConstant.BAOFOO_GATE + key;
    }
    
    /**
     * 充值费率
     * @return
     */
    public BigDecimal getFeePercentage(){
        return new BigDecimal(facade.getProperty(getFullKey("feePercentage"), "0.001"));
    }
    
    /**
     * 企业手续费
     * @return
     */
    public BigDecimal getEnterpriseFee(){
        return new BigDecimal(facade.getProperty(getFullKey("enterpriseFee"), "5"));
    }
    
    public String getRequestUrl() {
        return facade.getProperty(getFullKey("requestUrl"), "https://gw.baofoo.com/payindex");
    }
}
