package com.elend.gate.conf.facade.vo;

import com.elend.gate.conf.model.SCityPO;

public class SCity {
    public SCity() {
    }

    public SCity(SCityPO po) {
        this.cityId = po.getCityId();
        this.provinceId = po.getProvinceId();
        this.cityName = po.getCityName();
    }

    /** 城市id */
    private int cityId;

    /** 省份id */
    private int provinceId;

    /** 城市中文名称 */
    private String cityName;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

}
