package com.elend.gate.notify;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elend.gate.notify.model.NWithdrawQueuePO;
import com.elend.gate.notify.service.WithdrawQueueService;
import com.elend.gate.order.facade.OrderFacade;
import com.elend.p2p.Result;

/**
 * 提现的queue线程
 * @author mgt
 *
 */
public class WithdrawQueueThread extends Thread {
    private final static Logger log = LoggerFactory.getLogger(WithdrawQueueThread.class);

    /** 每次循环读取的 queue 数量 */
    protected int maxTasks;

    protected WithdrawQueueService withdrawQueueService;
    
    protected OrderFacade orderFacade;

    public WithdrawQueueThread(int maxTasks, WithdrawQueueService withdrawQueueService, OrderFacade orderFacade) {
        if (maxTasks < 10)
            maxTasks = 50;
        if (maxTasks > 1000)
            maxTasks = 1000;
        this.maxTasks = maxTasks;
        this.withdrawQueueService = withdrawQueueService;
        this.orderFacade = orderFacade;
    }

    @Override
    public void run() {
        //等待20S  等待基础数据初始化
        try {
            Thread.sleep(20000);
        } catch (Exception e) {
        }
        while (!QueueSetting.STOP_SERVER) {
            try { // Note: 只有 STOP_SERVER 标志可以终止线程
                log.info("Starting WithdrawQueueThread");
                runNormal();
            } catch (Exception e) { // 保证不会因为 exception 终止线程
                log.error(e.getMessage(), e);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e1) {
                }
            }
        }
        log.info("Stopping WithdrawQueueThread");
    }

    /**
     * 用于单线程
     */
    protected void runNormal() {
        while (!QueueSetting.STOP_SERVER) {
            while (QueueSetting.QUEUE_THREAD_WORKING/* || 1 == 1*/) {
                try {
                    //log.info("thread{} is running...",this.threadIndex);
                    // 每次读取一个 Queue 队列，按时间顺序 ( time <= now)
                    List<NWithdrawQueuePO> queues = withdrawQueueService.listWithdrawQueue(this.maxTasks);
                    if (queues != null && queues.size() > 0) {
                        for (NWithdrawQueuePO queue : queues) {
                            Result<String> result=null;
                            if (!QueueSetting.QUEUE_THREAD_WORKING/* && 0 == 1*/)
                                break;
                            try {
                                log.info("WithdrawQueueThread: executing Queue:{} ", queue);
                                //提现
                                result = withdrawQueueService.withdrawSingle(queue);
                                log.info("WithdrawQueueThread:  executing Queue_id: " + queue.getId() + ", result: " + result);
                            } catch (Exception e) {
                                log.error("running queue " + queue.getId() + ":" + e.getMessage(), e);
                            } finally {
                                //从队列中删除
                                int row = withdrawQueueService.deleteWithdrawQueue(queue.getId()); // 删除
                                log.info("提现队列删除, orderId:{}, row:{}", queue.getOrderId(), row);
								result=null;
                                queue=null;
                            }
                        }
                    } else { // 无 Queue, sleep 100ms
                        Thread.sleep(1000L);
                    }
                    queues=null;
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
