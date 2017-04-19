package com.elend.gate.order.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.order.service.PPayAgentApplyRequestService;
import com.elend.gate.order.vo.PPayAgentApplyRequestVO;

/**
 * 代收鉴权认证请求
 * 
 * @author mgt
 */
@Component
public class PPayAgentApplyRequestFacade {
    
    @Autowired
    private PPayAgentApplyRequestService service;
    
    public PPayAgentApplyRequestVO getByOrderId(String orderId) {
        return service.getByOrderId(orderId);
    }
}
