package com.elend.gate.notify;

import org.springframework.stereotype.Component;

/**
 * 2分钟重试一次，最多重试20次
 * @author liyongquan 2015年6月8日
 *
 */
@Component
public class SimpleRetryStategy implements RetryStategy{
    private static final int MAX_RETRY=20;
    private static final int TIME_INTERVAL=2*60;
    public int getTimeInterval(int retryTime){
        if(retryTime>=MAX_RETRY){
            return -1;
        }
        return TIME_INTERVAL;
    }
}
