package com.elend.gate.channel.util;

/**
 * 渠道选择策略
 * @author mgt
 */
public interface IChannelStrategy {

	/**
	 * 选择渠道
	 * @param bankId
	 * @return
	 */
	String getChannel(String bankId);
	
}
