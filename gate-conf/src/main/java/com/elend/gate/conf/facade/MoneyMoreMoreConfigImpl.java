package com.elend.gate.conf.facade;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.conf.constant.ChannelConfigConstant;
import com.elend.gate.conf.constant.GateConstant;
import com.elend.p2p.util.encrypt.ReversibleEncryptUtil;
@Component
public class MoneyMoreMoreConfigImpl implements MoneyMoreMoreConfig{
    @Autowired
    private PropertyFacade facade;
    
    @Autowired
    private GateConstant gateConstant;
    
    @Override
    public String getMerNo() {  
        String deMerNo=facade.getProperty(getFullKey("merNo"));;
        String merNo = ReversibleEncryptUtil.decrypt(deMerNo,
                                                      facade.getProperty("user_des3_key"),
                                                      facade.getProperty("user_aes_key"));
        return merNo;
                             // "168885");// 
    }

    @Override
    public String getRetrunReqURL() {
        return   String.format(facade.getProperty(getFullKey("retrunReqURL")),
                                     gateConstant.getDomain());
    }

    @Override
    public BigDecimal getFeePercentage() {
         return new BigDecimal(facade.getProperty(getFullKey("feePercentage"),"0.0015"));
    }

    
    /**
     * 获取数据库中完整的key
     * @param key
     * @return
     */
    private String getFullKey(String key){
        return ChannelConfigConstant.MONEY_MORE_MORE+key;
    }

    @Override
    public String getMd5Key() {
        String deMd5Key=facade.getProperty(getFullKey("md5Key"));
        String md5Key = ReversibleEncryptUtil.decrypt(deMd5Key,
                                                      facade.getProperty("user_des3_key"),
                                                      facade.getProperty("user_aes_key"));
        return md5Key; //;
    }

    @Override
    public String getCallbackUrl() {
        return String.format(facade.getProperty(getFullKey("callbackURL")),
                             gateConstant.getDomain());
    }

    @Override
    public String getRequestQueryUrl() {
        return String.format(facade.getProperty(getFullKey("requestQueryUrl")),
                             gateConstant.getDomain());
    }
    
    @Override
    public String getRequestUrl() {
        return String.format(facade.getProperty(getFullKey("requestUrl")),
                             gateConstant.getDomain());
    }
    
    
    public static void main(String[] args) {
      String deKey= ReversibleEncryptUtil.encrypt("168885",
                                                  "123457jhik3256789abcdefghijklmnopqrstuvwxyz@#$kjoisldrgl54r65q4gu87843918u%%4r0-*1qlkfl;ads7454dmnmkwaeriql1r4g57fgjdu893w842872y7u$",//SystemConstant.USER_DES3_KEY
                                                  "123456789abcdefg" );//SystemConstant.USER_AES_KEY
      System.out.println(deKey);
    }
}
