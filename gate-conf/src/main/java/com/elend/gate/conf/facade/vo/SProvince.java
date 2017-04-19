package com.elend.gate.conf.facade.vo;


import com.elend.gate.conf.model.SProvincePO;

public class SProvince {
    public SProvince() {
    }

    public SProvince(SProvincePO po) {
        this.provinceId = po.getProvinceId();
        this.provinceName = po.getProvinceName();
    }

    /** 省份id */
    private int provinceId;

    /** 省份 */
    private String provinceName;
    
    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

}
