package com.elend.gate.notify;

/**
 * 写入队列的选择策略
 * @author liyongquan 2015年6月4日
 *
 */
public interface QueueStrategy {
    /**
     * 获取队列序号
     * @param orderId--订单号
     * @return
     */
    int getQueueIndex(String orderId);
}
