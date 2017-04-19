package com.elend.gate.order.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.order.service.PPayAgentChargeRequestService;
import com.elend.gate.order.vo.PPayAgentChargeRequestSearchVO;
import com.elend.gate.order.vo.PPayAgentChargeRequestVO;
import com.elend.p2p.PageInfo;
import com.elend.p2p.Result;

/**
 * 提现请求
 * 
 * @author mgt
 */
@Component
public class PPayAgentChargeRequestFacade {
    
    @Autowired
    private PPayAgentChargeRequestService service;

    /**
     * 根据搜索条件返回列表
     * @param svo
     * @return
     */
    public Result<PageInfo<PPayAgentChargeRequestVO>> list(PPayAgentChargeRequestSearchVO vo) {
        return service.list(vo);
    }

    /**
     * 更新下次查询时间
     * @param seconds
     * @param orderId
     */
    public int updateNextQueryTime(int seconds, String orderId) {
        return service.updateNextQueryTime(seconds, orderId);
    }
}
