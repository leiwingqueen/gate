package com.elend.gate.order.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.elend.p2p.mapper.SqlMapper;
import com.elend.gate.order.vo.PPayAgentChargeRequestSearchVO;
import com.elend.gate.order.model.PPayAgentChargeRequestPO;

public interface PPayAgentChargeRequestMapper extends SqlMapper {

    /**
     * 根据搜索条件返回列表
     * 
     * @param svo
     * @return
     */
    List<PPayAgentChargeRequestPO> list(PPayAgentChargeRequestSearchVO svo);

    /**
     * 根据搜索条件返回列表总数
     * 
     * @param svo
     * @return
     */
    int count(PPayAgentChargeRequestSearchVO svo);

    /**
     * 根据主键id获取单条记录
     * 
     * @param id
     * @return
     */
    PPayAgentChargeRequestPO get(int id);

    /**
     * 插入记录
     * 
     * @param vo
     */
    int insert(PPayAgentChargeRequestPO po);

    /**
     * 更新记录
     * 
     * @param vo
     */
    int update(PPayAgentChargeRequestPO po);

    /**
     * 根据主键id删除记录
     * 
     * @param id
     */
    int delete(int id);

    /**
     * 根据合作平台订单号查询
     * @param name
     * @param partnerOrderId
     * @return
     */
    PPayAgentChargeRequestPO getByPartnerOrderId(@Param("partnerId")String partnerId,
            @Param("partnerOrderId")String partnerOrderId);

    /**
     * 回调更新请求记录
     * @param orderId
     * @param status
     * @param status2
     * @param requestUrl
     * @param bussflowno
     * @param resultStr
     * @param callbackStr
     * @param date
     * @return
     */
    int updateCallback(@Param("orderId")String orderId, @Param("oldStatus")int oldStatus, @Param("newStatus")int newStatus,
            @Param("requestUrl")String requestUrl, @Param("channelOrderId")String channelOrderId, @Param("resultStr")String resultStr,
            @Param("callbackStr")String callbackStr, @Param("callbackTime")Date callbackTime);

    /**
     * 根据订单号查询
     * @param name
     * @param partnerOrderId
     * @return
     */
    PPayAgentChargeRequestPO getByOrderId(@Param("orderId")String orderId);

    /**
     * 更新下次查询时间
     * @param nextQueryTime
     * @param orderId
     * @return
     */
    int updateNextQueryTime(@Param("nextQueryTime")Date nextQueryTime, @Param("orderId")String orderId);

}
