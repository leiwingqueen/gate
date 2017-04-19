package com.elend.gate.notify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器设置
 * 
 * @author liuxianyan
 */
public class QueueSetting {

    private final static Logger log = LoggerFactory.getLogger(QueueSetting.class);

    /**
     * 是否关闭服务器
     */
    public static boolean STOP_SERVER = false;

    /**
     * 用户 queu 线程是否在工作(启动了不一定在工作，可以在等待)
     */
    public static boolean QUEUE_THREAD_WORKING = false;
    
    /**
     * job 线程的数量,与job表对应(如job1,job2,jobn...等)
     */
    public static final int THREAD_COUNT = 3;

    /**
     * 设置服务器是否停止
     * 
     * @param stop
     */
    public static void setServerStop(boolean stop) {
        STOP_SERVER = stop;
        if (stop) {
            QUEUE_THREAD_WORKING = false;
        }

        log.info("设置服务器 stop = " + stop);
    }

    /**
     * 设置线程是否启动
     * 
     * @param start
     */
    public static void setJobThreadWorking(boolean working) {
        QUEUE_THREAD_WORKING = working;
        if (working) {
            STOP_SERVER = false;
        }
        log.info("设置线程 working = " + working);
    }
}
