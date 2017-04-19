package com.elend.gate.admin.service;

public interface RecMemcacheService {

	public void firstRequest(String userName);
	/**
	 * 用户退出系统后清理数据
	 */
	public void logOut(String userName);
	
	public void welcome(String userName);
}
