package com.elend.gate.order.service;

import com.elend.gate.order.vo.PPayAgentApplyRequestSearchVO;
import com.elend.gate.order.vo.PPayAgentApplyRequestVO;
import com.elend.p2p.PageInfo;
import com.elend.p2p.Result;

public interface PPayAgentApplyRequestService {

    /**
     * 根据搜索条件返回列表
     * 
     * @param svo
     * @return
     */
    Result<PageInfo<PPayAgentApplyRequestVO>> list(
            PPayAgentApplyRequestSearchVO vo);

    /**
     * 根据主键id获取单条记录
     * 
     * @param id
     * @return
     */
    Result<PPayAgentApplyRequestVO> get(int id);

    /**
     * 插入记录
     * 
     * @param vo
     */
    Result<PPayAgentApplyRequestVO> save(PPayAgentApplyRequestVO vo);

    /**
     * 根据主键id删除记录
     * 
     * @param id
     */
    Result<Integer> delete(int id);

    /**
     * 根据订单号查询
     * @param orderId
     * @return
     */
    PPayAgentApplyRequestVO getByOrderId(String orderId);

}
