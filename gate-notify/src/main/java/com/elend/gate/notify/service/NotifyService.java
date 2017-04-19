package com.elend.gate.notify.service;

import java.util.Date;
import java.util.List;

import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.gate.notify.model.NQueuePO;
import com.elend.p2p.Result;

/**
 * 通知模块
 * @author liyongquan 2015年6月3日
 *
 */
public interface NotifyService {
    /**
     * 消息通知入队
     * @param orderId--网关订单号
     * @param partner--接入方
     * @param partnerOrderId--接入方订单号
     * @param params--请求参数 url param格式
     * @param notifyUrl--点对点通知url
     * @param executeTime--发送时间
     * @param retryTime--发送次数
     */
    void addQueue(String orderId,PartnerConstant partner,String partnerOrderId,
            String params,String notifyUrl,Date executeTime,int retryTime);
    
    /**
     * 通知接入方
     * @param queue--队列
     * @return 
     */
    Result<String> notify(NQueuePO queue);
    
    /**
     * 消息重试
     * @param queue--消息
     * @param timeInterval--发送延时(单位秒)
     */
    void retry(NQueuePO queue,int timeInterval);
    
    /**
     * 获得 Queue 列表
     *@param queueIndex-队列ID
     * @param limit - 限制最大返回的个数
     * @return queue 列表或 size=0 的列表，列表排列顺序 execute_time (从低到高), queue_id (从小到大)
     */
    List<NQueuePO> listQueue(int queueIndex,int limit);
    
    /**
     * 从队列中删除
     * @param queueIndex--队列ID
     * @param id--订单号
     */
    void deleteQueue(int queueIndex,int id);
}
