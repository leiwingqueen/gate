package com.elend.gate.notify;

/**
 * 失败重试策略
 * @author liyongquan 2015年6月8日
 *
 */
public interface RetryStategy {
    /**
     * 获取重试的时间间隔(s)
     * @param retryTime--当前重试次数
     * @return >0:重试时间间隔,-1:不再进行重试
     */
    int getTimeInterval(int retryTime);
}
