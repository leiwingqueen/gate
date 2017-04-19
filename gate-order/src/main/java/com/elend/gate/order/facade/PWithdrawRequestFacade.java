package com.elend.gate.order.facade;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.conf.constant.PartnerConstant;
import com.elend.gate.order.model.PWithdrawRequestPO;
import com.elend.gate.order.service.PWithdrawRequestService;
import com.elend.gate.order.vo.PWithdrawRequestSearchVO;
import com.elend.gate.order.vo.PWithdrawRequestVO;
import com.elend.p2p.PageInfo;
import com.elend.p2p.Result;

/**
 * 提现请求
 * 
 * @author mgt
 */
@Component
public class PWithdrawRequestFacade {
    @Autowired
    private PWithdrawRequestService service;

    public PWithdrawRequestPO getByPartnerId(PartnerConstant partnerId,
            String partnerOrderId) {
        return service.getByPartnerId(partnerId, partnerOrderId);
    }

    /**
     * 按照创建时间查询
     * @param begin
     * @param end
     * @param currentPage
     * @param size
     * @return
     */
    public List<PWithdrawRequestPO> listByCreateTime(Date begin, Date end,
            int currentPage, int size) {
        return service.listByCreateTime(begin, end, currentPage, size);
    }

    /**
     * 分页查询
     * @param svo
     * @return
     */
    public Result<PageInfo<PWithdrawRequestVO>> queryPage(
            PWithdrawRequestSearchVO svo) {
        return service.queryPage(svo);
    }
    
    public PWithdrawRequestPO getByOrderId(String orderId) {
        return service.getByOrderId(orderId);
    }
}
