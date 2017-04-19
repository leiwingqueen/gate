package com.elend.gate.risk.vo;

/**
 * 风控请求参数
 * @author liyongquan 2015年11月6日
 *
 */
public class RiskParam {
    /**
     * 请求IP
     */
    private String ip;
    /**
     * http referer，标识用户来源
     */
    private String referer;
    /**
     * 时间戳
     */
    private long timeStamp;
    
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getReferer() {
        return referer;
    }
    public void setReferer(String referer) {
        this.referer = referer;
    }
    public long getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
