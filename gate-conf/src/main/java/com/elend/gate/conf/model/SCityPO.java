package com.elend.gate.conf.model;

import java.io.Serializable;

public class SCityPO implements Serializable {
	
    private static final long serialVersionUID = -3568128128004598944L;

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
