package com.elend.gate.order.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.elend.gate.order.model.PDepositRequestPO;
import com.elend.gate.order.vo.PDepositRequestSearchVO;
import com.elend.gate.order.vo.PDepositRequestVO;
import com.elend.p2p.PageInfo;
import com.elend.p2p.Result;

/**
 * 充值请求
 * @author mgt
 */
public interface PDepositRequestService {
	/**
	 * 按条件查询集合
	 * @param vo
	 * @return
	 */
	List<PDepositRequestPO> list(PDepositRequestSearchVO vo) ;
	/**
     * 根据主键id获取单条记录
     * 
     * @param orderId--订单号
     * @return
     */
    PDepositRequestPO getByOrderId(@Param("orderId")String orderId);
    
    /**
     * 列表查询
     * @param svo
     * @return
     */
    Result<PageInfo<PDepositRequestVO>> queryPage(PDepositRequestSearchVO svo);
}
