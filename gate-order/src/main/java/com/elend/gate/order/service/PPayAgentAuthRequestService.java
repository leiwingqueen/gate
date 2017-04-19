package com.elend.gate.order.service;

import com.elend.gate.order.vo.PPayAgentAuthRequestVO;
import com.elend.gate.order.vo.PPayAgentAuthRequestSearchVO;
import com.elend.p2p.Result;
import com.elend.p2p.PageInfo;

public interface PPayAgentAuthRequestService {

    /**
     * 根据搜索条件返回列表
     * 
     * @param svo
     * @return
     */
    Result<PageInfo<PPayAgentAuthRequestVO>> list(
            PPayAgentAuthRequestSearchVO vo);

    /**
     * 根据主键id获取单条记录
     * 
     * @param id
     * @return
     */
    Result<PPayAgentAuthRequestVO> get(int id);

    /**
     * 插入记录
     * 
     * @param vo
     */
    Result<PPayAgentAuthRequestVO> save(PPayAgentAuthRequestVO vo);

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
