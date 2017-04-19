package com.elend.gate.order.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.elend.p2p.mapper.SqlMapper;
import com.elend.gate.order.vo.PPayAgentApplyRequestSearchVO;
import com.elend.gate.order.model.PPayAgentApplyRequestPO;

public interface PPayAgentApplyRequestMapper extends SqlMapper {

    /**
     * 根据搜索条件返回列表
     * 
     * @param svo
     * @return
     */
    List<PPayAgentApplyRequestPO> list(PPayAgentApplyRequestSearchVO svo);

    /**
     * 根据搜索条件返回列表总数
     * 
     * @param svo
     * @return
     */
    int count(PPayAgentApplyRequestSearchVO svo);

    /**
     * 根据主键id获取单条记录
     * 
     * @param id
     * @return
     */
    PPayAgentApplyRequestPO get(int id);

    /**
     * 插入记录
     * 
     * @param vo
     */
    int insert(PPayAgentApplyRequestPO po);

    /**
     * 更新记录
     * 
     * @param vo
     */
    void update(PPayAgentApplyRequestPO po);

    /**
     * 根据主键id删除记录
     * 
     * @param id
     */
    void delete(int id);

    /**
     * 根据订单号查询
     * 
     * @param name
     * @param partnerOrderId
     * @return
     */
    PPayAgentApplyRequestPO getByPartnerOrderId(@Param("partnerId") String partnerId, @Param("partnerOrderId") String partnerOrderId);

    /**
     * 根据订单号获取请求记录
     * @param orderId
     * @return
     */
    PPayAgentApplyRequestPO getByOrderId(@Param("orderId")String orderId);

    /**
     * 第三方请求回调更新
     * @param orderId
     * @param status
     * @param newStatus
     * @param result
     * @param channelOrderId
     * @param requestUrl
     * @param merOrderId
     * @param custId
     * @param phoneToken
     * @param date
     * @return
     */
    int updateCallback(@Param("orderId")String orderId, @Param("oldStatus")int oldStatus, @Param("newStatus")int newStatus,
            @Param("result")String result, @Param("channelOrderId")String channelOrderId, @Param("requestUrl")String requestUrl,
            @Param("merOrderId")String merOrderId, @Param("custId")String custId, @Param("phoneToken")String phoneToken, @Param("updateTime")Date updateTime);
}
