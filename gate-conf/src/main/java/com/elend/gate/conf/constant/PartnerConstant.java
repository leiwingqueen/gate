package com.elend.gate.conf.constant;


/**
 * 接入支付网关的系统
 * @author liyongquan 2015年5月27日
 *
 */
public enum PartnerConstant {
    P2P("P2P平台");
    /**
     * 描述
     */
    private String desc;
    
    
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private PartnerConstant(String desc){
        this.desc=desc;
    }
    
    public static PartnerConstant from(String type) throws IllegalArgumentException{
        for (PartnerConstant one : values()) {
            if (one.name().equals(type))
                return one;
        }
        throw new IllegalArgumentException("illegal type：" + type);
    }
}
