package com.elend.gate.conf.mapper;

import java.util.List;

import com.elend.gate.conf.model.SCityPO;
import com.elend.p2p.mapper.SqlMapper;

public interface SCityMapper extends SqlMapper {

    /**
     * 根据省份返回该省份所有城市列表
     * 
     * @param provinceId
     * @return
     */
    List<SCityPO> list(int provinceId);
    /**
     * 保存城市列表
     * @param list
     * @return
     */
    int saveCitys(List<SCityPO> list);
    /**
     * 
     */
    int deleteAll();
    
    SCityPO get(int id);
}
