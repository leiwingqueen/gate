package com.elend.gate.notify.service;

import java.util.List;

import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.gate.notify.model.NWithdrawQueuePO;
import com.elend.p2p.Result;

/**
 * 通知模块
 * @author liyongquan 2015年6月3日
 *
 */
public interface WithdrawQueueService {
    
    /**
     * 插入提现队列
     * @param po
     * @return
     */
    int addWithdrawQueue(NWithdrawQueuePO po);

    /**
     * 查询提现的队列数据
     * @param maxTasks
     * @return
     */
    List<NWithdrawQueuePO> listWithdrawQueue(int limit);

    /**
     * 发送提现请求到第三方
     * @param queue
     * @return
     */
    Result<String> withdrawSingle(NWithdrawQueuePO queue);

    /**
     * 删除提现的队列
     * @param id
     * @return
     */
    int deleteWithdrawQueue(int id);

    /**
     * 获取提现队列记录
     * @param partnerId
     * @param partnerOrderId
     * @return
     */
    NWithdrawQueuePO getWithdrawQueue(PartnerConstant partnerId,
            String partnerOrderId);
}
