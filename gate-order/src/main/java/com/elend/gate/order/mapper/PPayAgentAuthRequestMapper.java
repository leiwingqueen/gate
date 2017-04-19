package com.elend.gate.order.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.elend.gate.order.model.PPayAgentAuthRequestPO;
import com.elend.gate.order.vo.PPayAgentAuthRequestSearchVO;
import com.elend.p2p.mapper.SqlMapper;

public interface PPayAgentAuthRequestMapper extends SqlMapper {

    /**
     * 根据搜索条件返回列表
     * 
     * @param svo
     * @return
     */
    List<PPayAgentAuthRequestPO> list(PPayAgentAuthRequestSearchVO svo);

    /**
     * 根据搜索条件返回列表总数
     * 
     * @param svo
     * @return
     */
    int count(PPayAgentAuthRequestSearchVO svo);

    /**
     * 根据主键id获取单条记录
     * 
     * @param id
     * @return
     */
    PPayAgentAuthRequestPO get(int id);

    /**
     * 插入记录
     * 
     * @param vo
     */
    int insert(PPayAgentAuthRequestPO po);

    /**
     * 更新记录
     * 
     * @param vo
     */
    void update(PPayAgentAuthRequestPO po);

    /**
     * 根据主键id删除记录
     * 
     * @param id
     */
    void delete(int id);

    /**
     * 根据合作方订单号查询
     * @param partnerId
     * @param partnerOrderId
     * @return
     */
    PPayAgentAuthRequestPO getByPartnerOrderId(@Param("partnerId")String partnerId,
            @Param("partnerOrderId")String partnerOrderId);

    /**
     * 根据订单号查询
     * @param orderId
     * @return
     */
    PPayAgentAuthRequestPO getByOrderId(@Param("orderId")String orderId);

    /**
     * 回调更新订单状态
     * @param orderId
     * @param status
     * @param signState
     * @param requestUrl
     * @param resultStr
     * @param callbackStr
     * @param date
     * @param date2
     * @return
     */
    int updateCheckCodeCallback(@Param("orderId")String orderId, @Param("oldStatus")int oldStatus,
            @Param("newStatus")int newStatus, @Param("requestUrl")String requestUrl, @Param("result")String result,
            @Param("callbackResult")String callbackResult, @Param("callbackTime")Date callbackTime, @Param("updteTime")Date updteTime);

    /**
     * 更新下次查询的时间
     * @param nextQueryTime
     * @param orderId
     * @return
     */
    int updateNextQueryTime(@Param("nextQueryTime")Date nextQueryTime, @Param("orderId")String orderId);

}
