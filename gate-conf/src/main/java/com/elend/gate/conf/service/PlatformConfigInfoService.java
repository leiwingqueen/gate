package com.elend.gate.conf.service;

import java.util.List;

import com.elend.gate.conf.facade.vo.SCity;
import com.elend.gate.conf.facade.vo.SProvince;
import com.elend.p2p.Result;

public interface PlatformConfigInfoService {

    /**
     * 返回所有省份列表
     * 
     * @return
     */
    Result<List<SProvince>> provinceList();

    /**
     * 根据省份id返回该省份所有城市列表
     * 
     * @param svo
     * @return
     */
    Result<List<SCity>> cityList(int provinceId);
    
    /**
     * 保存城市列表
     * @param cityList
     * @return
     */
    Result<Integer> saveCity(List<SCity> cityList);

    /**
     * 
     * @return
     */
    Result<Integer> deleteAllCity();
}
