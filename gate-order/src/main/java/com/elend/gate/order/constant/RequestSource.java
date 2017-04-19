package com.elend.gate.order.constant;
/**
 * 请求来源
 * 
 * @author linshumao
 *
 */
public enum RequestSource {
    WEB(1, "网站"),
    PHONE(2, "手机");
    private int source;
    private String desc;
    public int getSource() {
        return source;
    }
    public void setSource(int source) {
        this.source = source;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    private RequestSource(int source, String desc) {
        this.source = source;
        this.desc = desc;
    }
    public static RequestSource from(int source) {
        for (RequestSource one : values()) {
            if (one.getSource() == source) {
                return one;
            }
        }
        // 默认是web 来源
        return WEB;
    }
}
