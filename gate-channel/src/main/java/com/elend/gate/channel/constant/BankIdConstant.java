package com.elend.gate.channel.constant;


/**
 * 银行编号定义
 * @author liyongquan 2015年5月27日
 *
 */
public enum BankIdConstant {
    /**工商银行*/
    ICBC("102","工商银行"),
    /**招商银行*/
    CMBC("308","招商银行"),
    /**中国农业银行*/
    ABC("103","中国农业银行"),
    /**建设银行*/
    CCB("105","建设银行"),
    /**北京银行*/
    BCCB("31301","北京银行"),
    /**交通银行*/
    BOCO("301","交通银行"),
    /**兴业银行*/
    CIB("309","兴业银行"),
    /**光大银行*/
    CEB("303","光大银行"),
    /**中国银行*/
    BOC("104","中国银行"),
    /**平安银行*/
    PINGAN("307","平安银行"),
    /**中信银行*/
    ECITIC("302","中信银行"),
    /**深圳发展银行*/
    SDB("31302","深圳发展银行"),
    /**广发银行*/
    GDB("306","广发银行"),
    /**上海银行*/
    SHB("31303","上海银行"),
    /**上海浦东发展银行*/
    SPDB("310","上海浦东发展银行"),
    /**上海农商银行*/
    SNSB("322","上海农商银行"),
    /**中国邮政*/
    POST("403","中国邮政"),
    /**北京农村商业银行*/
    BJRCB("31401","北京农村商业银行"),
    /**华夏银行*/
    HXB("304","华夏银行"),
    /**温州银行*/
    WZB("31304","温州银行"),
    /**民生银行*/
    MSB("305","民生银行"),
    /**东亚银行*/
    BEA("502","东亚银行"),
    /**宁波银行*/
    NBB("31305","宁波银行"),
    /**南京银行*/
    BON("31306","南京银行"),
    /**成都银行*/
    CDB("31307","成都银行"),
    /**渤海银行*/
    BOHC("318","渤海银行"),
    /**广州农商银行*/
    GRCB("32201","广州农商银行"),    /**厦门银行*/
    XMCCB("31308","厦门银行"),
    /**江苏银行*/
    JSB("31309","江苏银行"),
    /**广州银行*/
    GZB("31310","广州银行"),
    /**宁夏银行*/
    NXB("31311","宁夏银行"),
    /**杭州银行*/
    HZB("31312","杭州银行"),
    /**长沙银行*/
    CSB("31313","长沙银行"),
    /**不指定银行*/
    NO_DESIGNATED("","不指定银行"),
    
    ;
    /**
     * 描述
     */
    private String desc;
    /**
     * 银行ID
     */
    private String bankId;
    
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private BankIdConstant(String bankId,String desc){
        this.bankId=bankId;
        this.desc=desc;
    }
    
    public static BankIdConstant from(String type) throws IllegalArgumentException{
        for (BankIdConstant one : values()) {
            if (one.getBankId().equals(type))
                return one;
        }
        throw new IllegalArgumentException("illegal type：" + type);
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }
}
