package com.elend.gate.notify.mapper;

import com.elend.gate.notify.model.NNotifyLogPO;
import com.elend.p2p.mapper.SqlMapper;

public interface NNotifyLogMapper extends SqlMapper {
    /**
     * 插入记录
     * 
     * @param vo
     */
    int insert(NNotifyLogPO po);
}
