package com.elend.gate.conf.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.elend.gate.conf.facade.vo.SCity;
import com.elend.gate.conf.facade.vo.SProvince;
import com.elend.gate.conf.mapper.SCityMapper;
import com.elend.gate.conf.mapper.SProvinceMapper;
import com.elend.gate.conf.model.SCityPO;
import com.elend.gate.conf.model.SProvincePO;
import com.elend.gate.conf.service.PlatformConfigInfoService;
import com.elend.p2p.Result;
import com.elend.p2p.constant.ResultCode;

@Service
public class PlatformConfigInfoServiceImpl implements
        PlatformConfigInfoService {

    private static Logger logger = LoggerFactory.getLogger(PlatformConfigInfoServiceImpl.class);

    @Autowired
    private SProvinceMapper sProvinceMapper;

    @Autowired
    private SCityMapper sCityMapper;

    @Override
    public Result<List<SProvince>> provinceList() {
        Result<List<SProvince>> result = new Result<List<SProvince>>();
        List<SProvincePO> list = sProvinceMapper.list();
        if (list.isEmpty()) {
            result.setCode(ResultCode.FAILURE);
            return result;
        }
        List<SProvince> provinces = new ArrayList<SProvince>();
        provinces.clear();
        for (SProvincePO po : list) {
            SProvince province = new SProvince();
            province.setProvinceId(po.getProvinceId());
            province.setProvinceName(po.getProvinceName());
            provinces.add(province);
        }
        result.setCode(ResultCode.SUCCESS);
        result.setObject(provinces);
        return result;
    }

    @Override
    public Result<List<SCity>> cityList(int provinceId) {
        Result<List<SCity>> result = new Result<List<SCity>>();
        List<SCityPO> list = sCityMapper.list(provinceId);
        
        List<SCity> cities = new ArrayList<SCity>(list.size());
        for (SCityPO po : list) {
            SCity city = new SCity();
            city.setCityId(po.getCityId());
            city.setCityName(po.getCityName());
            city.setProvinceId(po.getProvinceId());
            cities.add(city);
        }
        result.setCode(ResultCode.SUCCESS);
        result.setObject(cities);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Result<Integer> saveCity(List<SCity> cityList) {
        Result<Integer> result = new Result<Integer>();
        result.setCode(ResultCode.FAILURE);
        
        if (cityList == null || cityList.isEmpty()) {
            result.setMessage("城市列表不能为空");
            logger.info("s_city表保存数据失败");
            return result;
        }
        
        List<SCityPO> list = new ArrayList<SCityPO>(cityList.size());
        for (SCity city : cityList) {
            SCityPO po = new SCityPO();
            po.setCityId(city.getCityId());
            po.setProvinceId(city.getProvinceId());
            po.setCityName(city.getCityName());
            list.add(po);
        }
        int r = sCityMapper.saveCitys(list );
        
        result.setCode(ResultCode.SUCCESS);
        return result;
    }

    @Override
    public Result<Integer> deleteAllCity() {
        Result<Integer> result = new Result<Integer>();
        int r = sCityMapper.deleteAll();
        result.setCode(ResultCode.SUCCESS);
        return result;
    }
}
