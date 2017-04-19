package com.elend.gate.conf.mapper;

import java.util.List;

import com.elend.gate.conf.model.SProvincePO;
import com.elend.p2p.mapper.SqlMapper;

public interface SProvinceMapper extends SqlMapper {
	
	/**
	 * 返回所有省份列表
	 * @return
	 */
	List<SProvincePO> list();
	
	
	SProvincePO get(int id);
	

}
