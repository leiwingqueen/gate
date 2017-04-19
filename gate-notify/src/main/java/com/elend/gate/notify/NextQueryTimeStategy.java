package com.elend.gate.notify;

/**
 * 根据查询次数获取下次查询时间策略
 * @author mgt
 *
 */
public interface NextQueryTimeStategy {
    /**
     * 获取下次查询相隔的秒数
     * @param currentNum
     * @return
     */
    int getSeconds(int currentNum);
}
