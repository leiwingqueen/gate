package com.elend.gate.order.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.gate.order.model.POrderStatusRecrodPO;
import com.elend.p2p.mapper.SqlMapper;

public interface POrderStatusRecrodMapper extends SqlMapper {

    /**
     * 保存记录
     * @param statusPo
     * @return
     */
    int insert(POrderStatusRecrodPO statusPo);

    /**
     * 根据接入方订单号查询
     * @param partnerId
     * @param partnerOrderId
     * @return
     */
    POrderStatusRecrodPO getByPartnerOrderId(@Param("partnerId")PartnerConstant partnerId, @Param("partnerOrderId")String partnerOrderId);

    /**
     * 更新状态
     * @param orderId
     * @param status
     * @param date
     */
    void updateStatus(@Param("orderId")String orderId, @Param("newStatus")int newStatus, @Param("modifyTime")Date modifyTime);
    
}
