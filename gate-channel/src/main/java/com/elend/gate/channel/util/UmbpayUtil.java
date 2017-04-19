package com.elend.gate.channel.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 宝易互通充值的交互的一些工具方法
 * @author mgt
 */
public class UmbpayUtil {
	/**
	 * 功能：MD5加密
	 * @param strSrc 加密的源字符串
	 * @return 加密串 长度32位
	 */
	public static String GetMessageDigest(String strSrc) {
		MessageDigest md = null;
		String strDes = null;
		final String ALGO_MD5 = "MD5";

		byte[] bt = strSrc.getBytes();
		try {
			md = MessageDigest.getInstance(ALGO_MD5);
			md.update(bt);
			strDes = bytes2Hex(md.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(
				"系统不支持的MD5算法！");
		}
		return strDes;
	}

	/**
	 * 将字节数组转为HEX字符串(16进制串)
	 * @param bts 要转换的字节数组
	 * @return 转换后的HEX串
	 */
	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}
}
