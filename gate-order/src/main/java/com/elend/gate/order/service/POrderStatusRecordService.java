package com.elend.gate.order.service;

import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.gate.order.model.POrderStatusRecrodPO;


/**
 * 订单状态
 * @author mgt
 *
 */
public interface POrderStatusRecordService {

    /**
     * 保存记录
     * @param statusPo
     * @return
     */
    int save(POrderStatusRecrodPO statusPo);

    /**
     * 根据接入方订单号查询
     * @param partnerId
     * @param partnerOrderId
     * @return
     */
    POrderStatusRecrodPO getByPartnerOrderId(PartnerConstant partnerId,
            String partnerOrderId);
    
}
