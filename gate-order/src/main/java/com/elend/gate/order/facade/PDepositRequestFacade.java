package com.elend.gate.order.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.order.model.PDepositRequestPO;
import com.elend.gate.order.service.PDepositRequestService;
import com.elend.gate.order.vo.PDepositRequestSearchVO;
import com.elend.gate.order.vo.PDepositRequestVO;
import com.elend.p2p.PageInfo;
import com.elend.p2p.Result;

/**
 * 充值请求
 * @author mgt
 */
@Component
public class PDepositRequestFacade {
	@Autowired
	private PDepositRequestService service;
	
	/**
	 * 查询列表
	 * @param vo
	 * @return
	 */
	public List<PDepositRequestPO> list(PDepositRequestSearchVO vo) {
		return service.list(vo);
	}
	
	/**
	 * 根据Id查询
	 * @param orderId
	 * @return
	 */
	public PDepositRequestPO getByOrderId(String orderId) {
		return service.getByOrderId(orderId);
	}

    /**
     * 列表查询
     * @param svo
     * @return
     */
    public Result<PageInfo<PDepositRequestVO>> queryPage(
            PDepositRequestSearchVO svo) {
        return service.queryPage(svo);
    }
}
