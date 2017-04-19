package com.elend.gate.notify;

import org.springframework.stereotype.Component;

/**
 * 根据订单ID取模的选择策略
 * @author liyongquan 2015年6月15日
 *
 */
@Component("modQueueStrategy")
public class ModQueueStrategy implements QueueStrategy{

    @Override
    public int getQueueIndex(String orderId) {
        String last2=orderId.substring(orderId.length()-2);
        return Integer.parseInt(last2)%QueueSetting.THREAD_COUNT+1;
    }
}
