package com.elend.gate.order.mapper;

import java.util.List;

import com.elend.gate.order.model.PTransactionLogPO;
import com.elend.gate.order.vo.PTransactionLogSearchVO;
import com.elend.gate.order.vo.PTransactionLogVO;
import com.elend.gate.order.vo.TotalAssetInfo;
import com.elend.p2p.mapper.SqlMapper;

public interface PTransactionLogMapper extends SqlMapper {
    /**
     * 插入记录
     * 
     * @param vo
     */
    int insert(PTransactionLogPO po);

    /**
     * 根据ID删除
     * @param orderId
     * @return
     */
    int deleteByOrderId(String orderId);

    /**
     * 分页查询
     * @param svo
     * @return
     */
    List<PTransactionLogVO> list(PTransactionLogSearchVO svo);

    /**
     * 总记录数
     * @param svo
     * @return
     */
    int count(PTransactionLogSearchVO svo);

    /**
     * 查询总额
     * @param svo
     * @return
     */
    TotalAssetInfo getTotal(PTransactionLogSearchVO svo);
}
