package com.elend.gate.conf.facade;

import java.util.ArrayList;
import java.util.List;

import com.elend.p2p.util.encrypt.DesPropertiesEncoder;

/**
 * 初始化配置
 * @author liyongquan 2015年6月15日
 *
 */
public class ConfInit {
    private DesPropertiesEncoder encoder=new DesPropertiesEncoder();
    private List<Property> proList=new ArrayList<Property>(30);
    public static void main(String[] args) {
        ConfInit confInit=new ConfInit();
        confInit.add(confInit.new Property("P2P_priKey", "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl", "P2P对接的秘钥",true));
        confInit.add(confInit.new Property("referer_white_list", "www.gzdai.com", "referer白名单"));
        confInit.add(confInit.new Property("yeepay_callbackURL", "http://%s/yeepay/callback.jspx", "易宝回调地址"));
        confInit.add(confInit.new Property("yeepay_commonReqURL", "https://www.yeepay.com/app-merchant-proxy/node", ""));
        confInit.add(confInit.new Property("yeepay_merId", "10001126856", "易宝商户ID",true));
        confInit.add(confInit.new Property("yeepay_onlinePaymentReqURL", "https://www.yeepay.com/app-merchant-proxy/node", ""));
        confInit.add(confInit.new Property("yeepay_priKey", "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl", "易宝私钥",true));
        confInit.add(confInit.new Property("yeepay_queryRefundReqURL", "https://www.yeepay.com/app-merchant-proxy/command", ""));
        
        //连连移动端配置
        confInit.add(confInit.new Property("lianlian_mobile_merId", "201408071000001543", "连连SDK对接的商户ID"));
        confInit.add(confInit.new Property("lianlian_mobile_priKey", "201408071000001543test_20140812", "连连SDK对接的秘钥",true));
        confInit.add(confInit.new Property("lianlian_mobile_notifyUrl", "http://%s/lianlianMobile/callback.do", "连连SDK对接的秘钥"));
        confInit.genSql();
    }
    
    private String genSql(Property pro){
        String sqlFormat="insert into s_system_properties values('%s','%s','%s','admin',now(),now());";
        String value=pro.getValue();
        if(pro.isEnc){
            value=encoder.encode(pro.getValue());
        }
        String sql=String.format(sqlFormat, pro.getKey(),pro.getDesc(),value);
        System.out.println(sql);
        return sql;
    }
    
    public void genSql(){
        for(Property pro:proList){
            genSql(pro);
        }
    }
    
    public void add(Property pro){
        this.proList.add(pro);
    }
    
    public class Property{
        private String key;
        private String value;
        private String desc;
        private boolean isEnc=false;
        
        public Property(String key,String value,String desc,boolean isEnc){
            this.key=key;
            this.value=value;
            this.desc=desc;
            this.isEnc=isEnc;
        }
        public Property(String key,String value,String desc){
            this(key, value, desc, false);
        }
        public String getKey() {
            return key;
        }
        public void setKey(String key) {
            this.key = key;
        }
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
        public String getDesc() {
            return desc;
        }
        public void setDesc(String desc) {
            this.desc = desc;
        }
        public boolean isEnc() {
            return isEnc;
        }
        public void setEnc(boolean isEnc) {
            this.isEnc = isEnc;
        }
    }
}
