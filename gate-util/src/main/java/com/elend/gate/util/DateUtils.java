package com.elend.gate.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期操作工具类
 * @author mgt
 */
public class DateUtils {
	/**
	 * 获取指定格式的日期字符串
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDate(Date date, String format) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(date);
	}
	
	/**
	 * 获取指定格式的字符串
	 * @param date
	 * @param intervalDate  偏移天数  + 或 -
	 * @param format  格式
	 * @return
	 */
	public static String getDate(Date date, int intervalDay, int intervalHour, int intervalMin, int intervalSec, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, intervalDay);
		calendar.add(Calendar.HOUR, intervalDay);
		calendar.add(Calendar.MINUTE, intervalDay);
		calendar.add(Calendar.SECOND, intervalDay);
		return getDate(calendar.getTime(), format);
	}
	
	/**
	 * 获取指定格式的字符串
	 * @param date
	 * @param intervalDate  偏移天数  + 或 -
	 * @param format  格式
	 * @return
	 */
	public static Date getDate(Date date, int intervalDay, int intervalHour, int intervalMin, int intervalSec) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, intervalDay);
		calendar.add(Calendar.HOUR, intervalHour);
		calendar.add(Calendar.MINUTE, intervalMin);
		calendar.add(Calendar.SECOND, intervalSec);
		return calendar.getTime();
	}
}
