package com.elend.gate.conf.mapper;

import java.util.List;

import com.elend.gate.conf.model.SSystemPropertiesPO;
import com.elend.p2p.mapper.SqlMapper;

public interface SSystemPropertiesMapper extends SqlMapper {
    /**
     * 获取所有的配置
     * @return
     */
    List<SSystemPropertiesPO> listAll();

    /**
     * 根据propertyKey获取单条记录
     * 
     * @param propertyKey
     * @return
     */
    SSystemPropertiesPO get(String propertyKey);

    /**
     * 插入记录
     * 
     * @param vo
     */
    void insert(SSystemPropertiesPO po);

    /**
     * 更新记录
     * 
     * @param vo
     */
    void update(SSystemPropertiesPO po);

    /**
     * 根据propertyKey删除记录
     * 
     * @param id
     */
    void delete(String propertyKey);

}
