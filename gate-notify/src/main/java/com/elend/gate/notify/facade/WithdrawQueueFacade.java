package com.elend.gate.notify.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.gate.notify.model.NWithdrawQueuePO;
import com.elend.gate.notify.service.WithdrawQueueService;

/**
 * 提现队列
 * @author mgt
 *
 */
@Component
public class WithdrawQueueFacade {
    @Autowired
    private WithdrawQueueService withdrawQueueService;

    /**
     * 插入提现队列
     * @param po
     * @return
     */
    public int addWithdrawQueue(NWithdrawQueuePO po) {
        return withdrawQueueService.addWithdrawQueue(po);
    }

    /**
     * 获取提现队列记录
     * @param partnerId
     * @param partnerOrderId
     * @return
     */
    public NWithdrawQueuePO getWithdrawQueue(PartnerConstant partnerId,
            String partnerOrderId) {
        return withdrawQueueService.getWithdrawQueue(partnerId, partnerOrderId);
    }

}
