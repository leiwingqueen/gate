package com.elend.gate.conf.facade;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.conf.constant.ChannelConfigConstant;
import com.elend.gate.conf.constant.GateConstant;
import com.elend.p2p.util.encrypt.DesPropertiesEncoder;

/**
 * 连连网银支付配置
 * @author mgt
 * @date 2016年8月17日
 */
@Component
public class LianlianGateConfig{

    /**
     * 接口版本号
     */
    public static final String VERSION = "1.0";
    /**
     * 签名方式
     */
    public static final String SIGN_TYPE = "MD5";
    /**
     * 商户业务类型
     * 虚拟商品销售：101001
                    实物商品销售：109001
     */
    public static final String BUSI_PARTNER = "101001";
    /**
     * 商品名称
     */
    public static final String NAME_GOODS = "广州易贷充值";
    /**
     * 订单有效时间(分钟)
     */
    public static final int VALID_TIME = 30;
    
    
    @Autowired
    private PropertyFacade facade;
    @Autowired
    private DesPropertiesEncoder encoder;
    @Autowired
    private GateConstant gateConstant;
    
    @Autowired
    private LianlianMobileConfig lianlianMobileConfig;
    
    /**
     * 商户ID
     * @return
     */
    public String getMerId(){
        return lianlianMobileConfig.getMerId();
    }
    /**
     * 获取MD5方式加密KEY
     * @return
     */
    public String getPriKey(){
        return lianlianMobileConfig.getPriKey();
    }
    
    /**
     * 获取点对点通知url
     * @return
     */
    public String getNotifyUrl() {
        return String.format(facade.getProperty(getFullKey("notifyUrl"), "https://%s/lianlianGate/notify.do"), gateConstant.getDomain());
    }
    
    /**
     * 同步返回地址
     * @return
     */
    public String getReturnUrl() {
        return String.format(facade.getProperty(getFullKey("returnUrl"), "https://%s/lianlianGate/callback.jspx"), gateConstant.getDomain());
    }
    
    /**
     * 充值的订单查询地址
     * @return
     */
    public String getQueryUrl() {
        //return facade.getProperty(getFullKey("queryUrl"), "https://yintong.com.cn/queryapi/orderquery.htm");
        return facade.getProperty(getFullKey("queryUrl"), "https://queryapi.lianlianpay.com/orderquery.htm");
    }
    
    /**
     * 获取数据库中完整的key
     * @param key
     * @return
     */
    private String getFullKey(String key){
        return ChannelConfigConstant.LIANLIAN_GATE + key;
    }
    
    /**
     * 充值费率
     * @return
     */
    public BigDecimal getFeePercentage(){
        return new BigDecimal(facade.getProperty(getFullKey("feePercentage"), "0.0015"));
    }
    
    /**
     * 最高手续费
     * @return
     */
    public BigDecimal getFeeTop(){
        return new BigDecimal(facade.getProperty(getFullKey("feeTop"), "20"));
    }
    
    /**
     * 最低手续费
     * @return
     */
    public BigDecimal getFeeBottom(){
        return new BigDecimal(facade.getProperty(getFullKey("feeBottom"), "0.1"));
    }
    
    public String getRequestUrl() {
        return facade.getProperty(getFullKey("requestUrl"), "https://cashier.lianlianpay.com/payment/bankgateway.htm");
    }
}
