package com.elend.gate.risk;

/**
 * 分控开关
 * @author mgt
 *
 */
public class RiskSwitch {
    /**ip次数限制开关*/
    private boolean ipCountSwitch = true;
    /**ip白名单*/
    private boolean ipConsistentSwitch = true;
    /**来源开关*/
    private boolean refererSwitch = true;
    /**时间戳开关*/
    private boolean timeStampSwitch = true;
    
    public RiskSwitch(boolean ipCountSwitch, boolean ipConsistentSwitch,
            boolean refererSwitch, boolean timeStampSwitch) {
        super();
        this.ipCountSwitch = ipCountSwitch;
        this.ipConsistentSwitch = ipConsistentSwitch;
        this.refererSwitch = refererSwitch;
        this.timeStampSwitch = timeStampSwitch;
    }
    
    public boolean isIpCountSwitch() {
        return ipCountSwitch;
    }
    public void setIpCountSwitch(boolean ipCountSwitch) {
        this.ipCountSwitch = ipCountSwitch;
    }
    public boolean isIpConsistentSwitch() {
        return ipConsistentSwitch;
    }
    public void setIpConsistentSwitch(boolean ipConsistentSwitch) {
        this.ipConsistentSwitch = ipConsistentSwitch;
    }
    public boolean isRefererSwitch() {
        return refererSwitch;
    }
    public void setRefererSwitch(boolean refererSwitch) {
        this.refererSwitch = refererSwitch;
    }
    public boolean isTimeStampSwitch() {
        return timeStampSwitch;
    }
    public void setTimeStampSwitch(boolean timeStampSwitch) {
        this.timeStampSwitch = timeStampSwitch;
    }
    
}
