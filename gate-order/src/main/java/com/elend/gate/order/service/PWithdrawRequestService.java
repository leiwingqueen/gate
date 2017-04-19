package com.elend.gate.order.service;

import java.util.Date;
import java.util.List;

import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.gate.order.model.PWithdrawRequestPO;
import com.elend.gate.order.vo.PWithdrawRequestSearchVO;
import com.elend.gate.order.vo.PWithdrawRequestVO;
import com.elend.p2p.PageInfo;
import com.elend.p2p.Result;

/**
 * 提现请求
 * @author mgt
 */
public interface PWithdrawRequestService {

    /**
     * 根据接入方ID订单查询对应记录
     * @param partnerId
     * @param partnerOrderId
     * @return
     */
    PWithdrawRequestPO getByPartnerId(PartnerConstant partnerId,
            String partnerOrderId);

    /**
     * 按照创建时间查询
     * @param begin
     * @param end
     * @param currentPage
     * @param size
     * @return
     */
    List<PWithdrawRequestPO> listByCreateTime(Date begin, Date end,
            int currentPage, int size);

    /**
     * 分页查询
     * @param svo
     * @return
     */
    Result<PageInfo<PWithdrawRequestVO>> queryPage(
            PWithdrawRequestSearchVO svo);

    /**
     * 根据订单ID查询
     * @param orderId
     * @return
     */
    PWithdrawRequestPO getByOrderId(String orderId);
}
