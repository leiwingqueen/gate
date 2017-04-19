package com.elend.gate.notify.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.elend.gate.notify.model.NWithdrawQueuePO;
import com.elend.p2p.mapper.SqlMapper;

public interface NWithdrawQueueMapper extends SqlMapper {

    /**
     * 插入记录
     * 
     * @param vo
     */
    int insert(NWithdrawQueuePO po);

    /**
     * 查询指定行数的数据
     * @param limit
     * @param date
     * @return
     */
    List<NWithdrawQueuePO> list(@Param("limit")int limit, @Param("now")Date now);

    /**
     * 删除队列
     * @param id
     * @return
     */
    int delete(@Param("id")int id);

    /**
     * 
     * @param name
     * @param partnerOrderId
     * @return
     */
    NWithdrawQueuePO getByPertnerId(@Param("partnerId")String partnerId, @Param("partnerOrderId")String partnerOrderId);
}
