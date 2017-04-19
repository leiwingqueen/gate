package com.elend.gate.order.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.gate.order.model.POrderStatusRecrodPO;
import com.elend.gate.order.service.POrderStatusRecordService;

@Component
public class POrderStatusRecordFacade {
    @Autowired
    private POrderStatusRecordService service;

    /**
     * 保存记录
     * @param statusPo
     * @return
     */
    public int save(POrderStatusRecrodPO statusPo) {
        return service.save(statusPo);
    }

    /**
     * 根据接入方订单号查询
     * @param partnerId
     * @param partnerOrderId
     * @return
     */
    public POrderStatusRecrodPO getByPartnerOrderId(PartnerConstant partnerId, String partnerOrderId) {
        return service.getByPartnerOrderId(partnerId, partnerOrderId);
    }
}
