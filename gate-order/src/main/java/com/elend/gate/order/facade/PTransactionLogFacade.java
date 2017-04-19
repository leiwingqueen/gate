package com.elend.gate.order.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.order.service.PTransactionLogService;
import com.elend.gate.order.vo.PTransactionLogSearchVO;
import com.elend.gate.order.vo.PTransactionLogVO;
import com.elend.gate.order.vo.TotalAssetInfo;
import com.elend.p2p.PageInfo;
import com.elend.p2p.Result;

/**
 * 充值请求
 * @author mgt
 */
@Component
public class PTransactionLogFacade {
    @Autowired
    private PTransactionLogService service;

    /**
     * 按页查询
     * @param svo
     * @return
     */
    public Result<PageInfo<PTransactionLogVO>> queryPage(
            PTransactionLogSearchVO svo) {
        return service.queryPage(svo);
    }

    /**
     * 查询总额
     * @param svo
     * @return
     */
    public Result<TotalAssetInfo> getTotal(PTransactionLogSearchVO svo) {
        return service.getTotal(svo);
    }
}
