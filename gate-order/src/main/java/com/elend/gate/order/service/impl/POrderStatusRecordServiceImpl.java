package com.elend.gate.order.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.gate.order.mapper.POrderStatusRecrodMapper;
import com.elend.gate.order.model.POrderStatusRecrodPO;
import com.elend.gate.order.service.POrderStatusRecordService;

@Service
public class POrderStatusRecordServiceImpl implements POrderStatusRecordService{
    private final static Logger logger = LoggerFactory.getLogger(POrderStatusRecordServiceImpl.class);
    
    @Autowired
    private POrderStatusRecrodMapper mapper;

    @Override
    public int save(POrderStatusRecrodPO statusPo) {
        return mapper.insert(statusPo);
    }

    @Override
    public POrderStatusRecrodPO getByPartnerOrderId(
            PartnerConstant partnerId, String partnerOrderId) {
        return mapper.getByPartnerOrderId(partnerId, partnerOrderId);
    }
}
