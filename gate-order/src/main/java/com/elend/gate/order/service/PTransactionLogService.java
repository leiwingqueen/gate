package com.elend.gate.order.service;

import com.elend.gate.order.vo.PTransactionLogSearchVO;
import com.elend.gate.order.vo.PTransactionLogVO;
import com.elend.gate.order.vo.TotalAssetInfo;
import com.elend.p2p.PageInfo;
import com.elend.p2p.Result;


/**
 * 充值请求
 * @author mgt
 */
public interface PTransactionLogService {

    /**
     * 分页查询
     * @param svo
     * @return
     */
    Result<PageInfo<PTransactionLogVO>> queryPage(PTransactionLogSearchVO svo);

    /**
     * 查询总额
     * @param svo
     * @return
     */
    Result<TotalAssetInfo> getTotal(PTransactionLogSearchVO svo);

}
