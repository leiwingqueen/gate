package com.elend.gate.order.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.elend.gate.order.model.PDepositRequestPO;
import com.elend.gate.order.vo.PDepositRequestSearchVO;
import com.elend.p2p.mapper.SqlMapper;

public interface PDepositRequestMapper extends SqlMapper {
    /**
     * 根据主键id获取单条记录
     * 
     * @param orderId--订单号
     * @return
     */
    PDepositRequestPO getByOrderId(@Param("orderId")String orderId);
    
    /**
     * 根据接入方订单号获取单条记录
     * 
     * @param partnerId--接入方ID
     * @param partnerTradeNo--接入方订单号
     * @return
     */
    PDepositRequestPO getByPartnerTradeNo(@Param("partnerId")String partnerId,@Param("partnerTradeNo")String partnerTradeNo);

    /**
     * 插入记录
     * 
     * @param vo
     */
    int insert(PDepositRequestPO po);
    
    /**
     * 状态更新
     * @param orderId--订单号
     * @param oldStatus--原来的状态
     * @param newStatus--新状态
     * @param callBackTime--回调时间
     * @param callbackData--回调数据
     * @param channelTradeNo--渠道订单号
     * @return
     */
    int updateStatus(@Param("orderId")String orderId, @Param("oldStatus")int oldStatus,@Param("newStatus") int newStatus,
            @Param("callBackTime")Date callBackTime,@Param("callbackData")String callbackData,@Param("channelTradeNo")String channelTradeNo, @Param("payType")int payType);
    
    /**
     * 查询集合
     * @param vo
     * @return
     */
    List<PDepositRequestPO> list(PDepositRequestSearchVO vo) ;
    
    /**
     * 查询集合(时间降序)
     * @param vo
     * @return
     */
    List<PDepositRequestPO> listPage(PDepositRequestSearchVO vo) ;

    /**
     * 总记录数
     * @param svo
     * @return
     */
    int count(PDepositRequestSearchVO svo);
}
