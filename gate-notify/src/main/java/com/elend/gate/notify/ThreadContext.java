package com.elend.gate.notify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elend.gate.notify.service.NotifyService;
import com.elend.gate.notify.service.PayAgentCallbackService;
import com.elend.gate.notify.service.WithdrawQueueService;
import com.elend.gate.order.facade.OrderFacade;

/**
 * 线程上下文
 * @author liyongquan 2015年6月5日
 *
 */
public class ThreadContext {

    private final static Logger log = LoggerFactory.getLogger(ThreadContext.class);

    private Thread[] threads = null;

    public static final int JOB_COUNT_PER_LOOP = 100; // 每个循环读的任务数量,可以根据实际情况调整
    
    private NotifyService notifyService;
    
    private OrderFacade orderFacade;
    
    private RetryStategy retryStategy;
    
    private WithdrawQueueService withdrawQueueService;
    
    private PayAgentCallbackService payAgentCallbackService;

    /**
     * 启动线程
     */
    public void start() {
        try {
            // 1. 启动普通 job 线程
            threads = new Thread[QueueSetting.THREAD_COUNT + 1 + 1];
            for (int i = 0; i < QueueSetting.THREAD_COUNT; i++) {
                int threadIndex=i+1;
                threads[i] = new QueueThread(threadIndex, JOB_COUNT_PER_LOOP,notifyService,orderFacade,retryStategy);
            }
            
            //创建提现队列的线程
            threads[threads.length - 2] = new WithdrawQueueThread(JOB_COUNT_PER_LOOP, withdrawQueueService, orderFacade);
            threads[threads.length - 1] = new PayAgentCallbackThread(payAgentCallbackService);
            
            log.info("threads.length=====" + threads.length);
            for (int i = 0; i < threads.length; i++) {
                threads[i].start();
            }
            
            //QueueSetting.setJobThreadWorking(true);
        } catch (Exception e) {
            log.error("线程启动失败,"+e.getMessage(),e);
            stop();
            log.error("线程停止工作...");
        }
    }
    
    public void stop(){
        QueueSetting.setServerStop(false);
    }

    public NotifyService getNotifyService() {
        return notifyService;
    }

    public void setNotifyService(NotifyService notifyService) {
        this.notifyService = notifyService;
    }

    public OrderFacade getOrderFacade() {
        return orderFacade;
    }

    public void setOrderFacade(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    public RetryStategy getRetryStategy() {
        return retryStategy;
    }

    public void setRetryStategy(RetryStategy retryStategy) {
        this.retryStategy = retryStategy;
    }

    public WithdrawQueueService getWithdrawQueueService() {
        return withdrawQueueService;
    }

    public void setWithdrawQueueService(WithdrawQueueService withdrawQueueService) {
        this.withdrawQueueService = withdrawQueueService;
    }

    public void setPayAgentCallbackService(
            PayAgentCallbackService payAgentCallbackService) {
        this.payAgentCallbackService = payAgentCallbackService;
    }

}
