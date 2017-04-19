package com.elend.gate.order.service;

import com.elend.gate.order.vo.PPayAgentChargeRequestVO;
import com.elend.gate.order.vo.PPayAgentChargeRequestSearchVO;
import com.elend.p2p.Result;
import com.elend.p2p.PageInfo;

public interface PPayAgentChargeRequestService {

    /**
     * 根据搜索条件返回列表
     * 
     * @param svo
     * @return
     */
    Result<PageInfo<PPayAgentChargeRequestVO>> list(
            PPayAgentChargeRequestSearchVO vo);

    /**
     * 根据主键id获取单条记录
     * 
     * @param id
     * @return
     */
    Result<PPayAgentChargeRequestVO> get(int id);

    /**
     * 插入记录
     * 
     * @param vo
     */
    Result<PPayAgentChargeRequestVO> save(PPayAgentChargeRequestVO vo);

    /**
     * 根据主键id删除记录
     * 
     * @param id
     */
    Result<Integer> delete(int id);

    /**
     * 更新下次查询的时间
     * @param seconds
     * @param orderId
     * @return
     */
    int updateNextQueryTime(int seconds, String orderId);

}
