package com.elend.gate.admin.formatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.format.Formatter;

/**
 * spring 日期转换格式化
 * 
 * @author mgt
 */
public class DateFormatter implements Formatter<Date> {

    @Override
    public String print(Date object, Locale locale) {
        return null;
    }

    @Override
    public Date parse(String text, Locale locale)
            throws java.text.ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(text);
        } catch (Exception e) {
            format = new SimpleDateFormat("yyyy-MM-dd");
            date = format.parse(text);
        }
        return date;
    }

}
