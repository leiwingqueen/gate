package com.elend.gate.order.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.order.service.PPayAgentAuthRequestService;
import com.elend.gate.order.vo.PPayAgentAuthRequestSearchVO;
import com.elend.gate.order.vo.PPayAgentAuthRequestVO;
import com.elend.p2p.PageInfo;
import com.elend.p2p.Result;

/**
 * 代收鉴权认证请求
 * 
 * @author mgt
 */
@Component
public class PPayAgentAuthRequestFacade {
    
    @Autowired
    private PPayAgentAuthRequestService service;
    
    /**
     * 根据搜索条件返回列表
     * @param svo
     * @return
     */
    public Result<PageInfo<PPayAgentAuthRequestVO>> list(PPayAgentAuthRequestSearchVO vo) {
        return service.list(vo);
    }

    /**
     * 更新下次的查询时间
     * @param seconds
     */
    public int updateNextQueryTime(int seconds, String orderId) {
        return service.updateNextQueryTime(seconds, orderId);
    }
}
