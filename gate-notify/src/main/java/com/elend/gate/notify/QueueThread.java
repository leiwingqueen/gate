package com.elend.gate.notify;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elend.gate.notify.model.NQueuePO;
import com.elend.gate.notify.service.NotifyService;
import com.elend.gate.order.constant.NotifyStatus;
import com.elend.gate.order.facade.OrderFacade;
import com.elend.p2p.Result;

/**
 * queue线程
 * @author liyongquan 2015年6月4日
 *
 */
public class QueueThread extends Thread {
    private final static Logger log = LoggerFactory.getLogger(QueueThread.class);

    /** 每次循环读取的 queue 数量 */
    protected int maxTasks;

    protected NotifyService notifyService;
    
    protected OrderFacade orderFacade;
    
    protected RetryStategy retryStategy;

    /** 线程的 index */
    protected int threadIndex = 1;

    /** 已启动的线程 index */
    public static Map<Integer, Boolean> startedThread = new HashMap<Integer, Boolean>();

    public QueueThread(int threadIndex, int maxTasks,
            NotifyService notifyService,OrderFacade orderFacade,RetryStategy retryStategy) {
        if (maxTasks < 10)
            maxTasks = 50;
        if (maxTasks > 1000)
            maxTasks = 1000;
        this.maxTasks = maxTasks;
        this.threadIndex = threadIndex;
        if (!isValidThreadIndex(threadIndex)
                || (startedThread.containsKey(threadIndex) && startedThread.get(threadIndex)))
            throw new IllegalArgumentException("threadIndex");
        startedThread.put(threadIndex, Boolean.TRUE); // 记录此线程已启动
        this.notifyService=notifyService;
        this.orderFacade=orderFacade;
        this.retryStategy=retryStategy;
    }

    /**
     * 判断线程的 index 是否有效
     * 
     * @param index
     * @return
     */
    public static boolean isValidThreadIndex(int index) {
        return (index > 0 && index <= QueueSetting.THREAD_COUNT);
    }

    @Override
    public void run() {
        while (!QueueSetting.STOP_SERVER) {
            try { // Note: 只有 STOP_SERVER 标志可以终止线程
                log.info("Starting thread: " + threadIndex);
                runNormal();
            } catch (Exception e) { // 保证不会因为 exception 终止线程
                log.error(e.getMessage(), e);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e1) {
                }
            }
        }
        log.info("Stopping thread: " + threadIndex);
    }

    /**
     * 用于单线程
     */
    protected void runNormal() {
        while (!QueueSetting.STOP_SERVER) {
            while (QueueSetting.QUEUE_THREAD_WORKING) {
                try {
                    //log.info("thread{} is running...",this.threadIndex);
                    // 每次读取一个 Queue 队列，按时间顺序 ( time <= now)
                    List<NQueuePO> queues = notifyService.listQueue(this.threadIndex,
                                                                  this.maxTasks);
                    if (queues != null && queues.size() > 0) {
                        for (NQueuePO queue : queues) {
                            if (!QueueSetting.QUEUE_THREAD_WORKING)
                                break;
                            try {
                                if (log.isDebugEnabled())
                                    log.debug("thread " + this.threadIndex
                                            + ": executing Queue "
                                            + queue.getId() + " ...");
                                //发送通知
                                Result<String> result = notifyService.notify(queue);
                                log.info("thread: " + this.threadIndex
                                        + ", executing Queue_id: "
                                        + queue.getId() + ", result: "
                                        + result);
                                //更新订单的通知状态
                                orderFacade.updateNotifyStatus(queue.getOrderId(),
                                                               result.isSuccess()?NotifyStatus.SUCCESS:NotifyStatus.FAIL);
                                //失败重新入队列
                                if (!result.isSuccess()) {
                                    int interval=retryStategy.getTimeInterval(queue.getRetryTimes());
                                    if(interval>0){
                                        notifyService.retry(queue,
                                                            interval);
                                    }
                                }
                            } catch (Exception e) {
                                log.error(e.getMessage(), e);
                                //出现异常重新入队列
                                int interval=retryStategy.getTimeInterval(queue.getRetryTimes());
                                if(interval>0){
                                    notifyService.retry(queue,
                                                        interval);
                                }
                            } finally {
                                //从队列中删除
                                notifyService.deleteQueue(this.threadIndex,
                                                   queue.getId()); // 删除
                            }
                        }
                    } else { // 无 Queue, sleep 100ms
                        Thread.sleep(100L);
                    }
                } catch (Exception e) {
                    log.error("系统未知错误," + e.getMessage(), e);
                }
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    log.error("系统未知错误," + e.getMessage(), e);
                }
            }
            try {
                Thread.sleep(250L); // Queue 线程未开始工作，sleep 250ms
            } catch (InterruptedException e) {
                log.error("系统未知错误," + e.getMessage(), e);
            }
        }
    }
}
