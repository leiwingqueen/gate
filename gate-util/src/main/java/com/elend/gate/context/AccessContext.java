package com.elend.gate.context;

/**
 * 存放访问相关的信息(ip、来源等)
 * 
 * @author liyongquan 2015年6月2日
 */
public class AccessContext {
    /**
     * 访问IP
     */
    private static ThreadLocal<String> accessIpHolder = new ThreadLocal<String>();

    /**
     * 访问来源 1.网页 2.手机
     */
    private static ThreadLocal<Integer> accessSourceHolder = new ThreadLocal<Integer>();
    /**
     * http referer
     */
    private static ThreadLocal<String> refererHolder = new ThreadLocal<String>();
    /**
     * 用户浏览器版本信息
     */
    private static ThreadLocal<String> userAgentHolder = new ThreadLocal<String>();
    
    public static String getReferer(){
        return refererHolder.get()==null?"":refererHolder.get();
    }
    

    /**
     * 获取访问IP
     * @return
     */
    public static String getAccessIp() {
        return accessIpHolder.get()==null?"":accessIpHolder.get();
    }
    
    /**
     * 获取访问来源
     * @return
     */
    public static int getAccessSource(){
        return accessSourceHolder.get()==null?0:accessSourceHolder.get().intValue();
    }
    /**
     * 获取用户浏览器版本
     * @return
     */
    public static String getUserAgent(){
        return userAgentHolder.get()==null?"":userAgentHolder.get();
    }

    // --------------------------- setters

    /**
     * 设置IP
     * @param ip
     */
    public static void setAccessIp(String ip) {
        accessIpHolder.set(ip);
    }

    /**
     * 设置访问来源
     * @param source
     */
    public static void setAccessSource(int source) {
        accessSourceHolder.set(source);
    }
    
    public static void setReferer(String referer){
        refererHolder.set(referer);
    }
    
    public static void setUserAgent(String userAgent){
        userAgentHolder.set(userAgent);
    }

    // --------------------------- clear

    /**
     * 清空所有数据
     */
    public static void clear() {
        accessIpHolder.set(null);
        accessSourceHolder.set(null);
        refererHolder.set(null);
        userAgentHolder.set(null);
    }
}
