package com.elend.gate.order.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.gate.order.model.POrderPO;
import com.elend.p2p.mapper.SqlMapper;

public interface POrderMapper extends SqlMapper {
    /**
     * 根据订单ID获取单条记录
     * 
     * @param orderId
     * @return
     */
    POrderPO getByOrderId(@Param("orderId")String orderId);

    /**
     * 插入记录
     * 
     * @param vo
     */
    int insert(POrderPO po);
    /**
     * 更新通知状态
     * @param orderId--订单号
     * @param oldStatus--原来的状态
     * @param noticeStatus--通知状态
     * @param finishTime--完成时间
     * @return
     */
    int updateNoticeStatus(@Param("orderId")String orderId,
            @Param("notifyStatus")int notifyStatus,
            @Param("finishTime")Date finishTime);

    /**
     * 获取订单记录
     * @param partnerId
     * @param partnerOrderId
     * @return
     */
    POrderPO getOrderBypartnerOrderId(@Param("partnerId")PartnerConstant partnerId, @Param("partnerOrderId")String partnerOrderId);

    /**
     * 查询没有余额流水的订单（初始化）
     * @param i
     * @return
     */
    List<POrderPO> listNoLog(@Param("startTime")Date startTime, @Param("row")int row);
}
