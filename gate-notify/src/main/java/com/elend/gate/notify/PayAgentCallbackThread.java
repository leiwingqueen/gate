package com.elend.gate.notify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elend.gate.notify.service.PayAgentCallbackService;
import com.elend.p2p.Result;

/**
 * 代收查询第三方并回调P2P
 * @author mgt
 *
 */
public class PayAgentCallbackThread extends Thread {
    private final static Logger log = LoggerFactory.getLogger(PayAgentCallbackThread.class);

    protected PayAgentCallbackService service;

    public PayAgentCallbackThread(PayAgentCallbackService service) {
        this.service = service;
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
                log.info("Starting PayAgentCallbackThread");
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
            while (QueueSetting.QUEUE_THREAD_WORKING) {
                try {
                    Result<String> result = service.queryAndCallback();
                    //log.info("回调结果:{}", result);
                } catch (Exception e) {
                    log.error("系统未知错误," + e.getMessage(), e);
                }
                try {
                    Thread.sleep(15000L);
                } catch (InterruptedException e) {
                }
            }
            try {
                Thread.sleep(1000L); // Queue 线程未开始工作，sleep 250ms
            } catch (InterruptedException e) {
            }
        }
    }
}
