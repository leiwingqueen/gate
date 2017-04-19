package com.elend.gate.conf.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.conf.facade.vo.SCity;
import com.elend.gate.conf.facade.vo.SProvince;
import com.elend.gate.conf.service.PlatformConfigInfoService;
import com.elend.p2p.Result;

/**
 * 查找所有省份、省份城市、分行信息
 * 
 * @author lxl
 */
@Component
public class PlatformConfigInfoFacade {

    @Autowired
    private PlatformConfigInfoService platformConfigInfoService;

    /**
     * 返回所有省份列表
     * 
     * @return
     */
    public Result<List<SProvince>> provinceList() {
        return platformConfigInfoService.provinceList();
    }

    /**
     * 根据省份id返回该省份所有城市列表
     * 
     * @param svo
     * @return
     */
    public Result<List<SCity>> cityList(int provinceId) {
        return platformConfigInfoService.cityList(provinceId);
    }

    /**
     * @param cityList
     * @return
     */
    public Result<Integer> saveCity(List<SCity> cityList) {
        return platformConfigInfoService.saveCity(cityList);
    }

    public Result<Integer> deleteAllCity() {
        return platformConfigInfoService.deleteAllCity();
    }
}
