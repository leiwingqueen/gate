package com.elend.gate.order.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.elend.gate.order.model.PWithdrawRequestPO;
import com.elend.gate.order.vo.PWithdrawRequestSearchVO;
import com.elend.gate.order.vo.PWithdrawRequestVO;
import com.elend.p2p.mapper.SqlMapper;

public interface PWithdrawRequestMapper extends SqlMapper {
    /**
     * 根据主键id获取单条记录
     * 
     * @param orderId--订单号
     * @return
     */
    PWithdrawRequestPO getByOrderId(@Param("orderId")String orderId);
    
    /**
     * 根据接入方订单号获取单条记录
     * 
     * @param partnerId--接入方ID
     * @param partnerTradeNo--接入方订单号
     * @return
     */
    PWithdrawRequestPO getByPartnerOrderId(@Param("partnerId")String partnerId,@Param("partnerOrderId")String partnerOrderId);

    /**
     * 插入记录
     * 
     * @param vo
     */
    int insert(PWithdrawRequestPO po);
    
    /**
     * 状态更新
     * @param orderId           订单号
     * @param oldStatus         老状态
     * @param newStatus         新状态
     * @param callBackTime      回调时间
     * @param callbackData      回调数据
     * @param requestData
     * @param channelTradeNo
     * @return
     */
    int update(@Param("orderId")String orderId, @Param("oldStatus")int oldStatus, @Param("newStatus") int newStatus,
            @Param("callbackTime")Date callbackTime, @Param("result")String result, @Param("callbackResult")String callbackResult, @Param("channelOrderId")String channelOrderId, @Param("requestUrl")String requestUrl);

    /**
     * 按照创建时间查询
     * @param begin
     * @param end
     * @param currentPage
     * @param size
     * @return
     */
    List<PWithdrawRequestPO> listPageByCreateTime(@Param("begin")Date begin, @Param("end")Date end, @Param("start")int start, @Param("size")int size);

    /**
     * 查询
     * @param svo
     * @return
     */
    List<PWithdrawRequestVO> list(PWithdrawRequestSearchVO svo);

    /**
     * 总记录数
     * @param svo
     * @return
     */
    int count(PWithdrawRequestSearchVO svo);

    /**
     * 更新渠道订单号
     * @param orderId
     * @param channelOrderId
     * @return
     */
    int updateChannelOrderId(@Param("orderId")String orderId, @Param("channelOrderId")String channelOrderId);
}
