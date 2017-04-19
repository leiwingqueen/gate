package com.elend.gate.conf.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 重读系统配置的线程. 之前使用spring定时运行TimerTask，有发生reload失败的问题。改用单线程运行。
 * thread 由InitPayServlet启动。
 * 
 * @author linyi 2013/6/3
 */
public class ReloadDataThread extends Thread {

    private Logger logger = LoggerFactory.getLogger(ReloadDataThread.class);

    /**
     * 线程是否在工作中
     */
    private boolean working;

    /** RELOAD间隔：90s */
    private int interval = 90000;

    private ReloadDataThread() {
        super("ReloadDataThread");
    }
    
    /**
     * 只能有一个实例
     */
    private static ReloadDataThread instance = new ReloadDataThread();
    
    /**
     * 获得实例
     * @return
     */
    public static ReloadDataThread getInstance() {
        return instance;
    }

    public void stopWorking() {
        working = false;
        interrupt();
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        if (interval > 1) {
            this.interval = interval;
        }
    }

    @Override
    public void run() {
        working = true;
        logger.info(super.getName() + " started ...");
        while (working) {
            try {
                long t1 = System.currentTimeMillis();
                // reload
                InitData.reload();
                long time = System.currentTimeMillis() - t1;
                logger.info("Data reloaded successfully, time=" + time + " ms");
            } catch (Exception e) {
                logger.error("Data reloaded failed: " + e.getMessage(), e);
            }

            try {
                // sleep在一个独立的try{} catch{}块里，即使上面程序总是出错，也不会造成死循环。
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                logger.info(super.getName() + " was interrupted.");
            }
        }
        logger.info(super.getName() + " stopped ...");
    }
}
