package com.elend.gate.conf.model;

import java.io.Serializable;

public class SProvincePO implements Serializable {
	
    private static final long serialVersionUID = -3568128308004804944L;

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
