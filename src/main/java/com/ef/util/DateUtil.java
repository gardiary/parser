package com.ef.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gardiary on 02/04/18.
 */
public class DateUtil {

    public static final DateFormat DATE_FORMAT_1 = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
    public static final DateFormat DATE_FORMAT_2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static final DateFormat DATE_FORMAT_3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Date addHours(Date date, Integer hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public static Date addDays(Date date, Integer days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static Date parse(String dateStr, DateFormat format) {
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Date string is not valid");
        }
    }

    public static Date parse1(String dateStr) {
        return parse(dateStr, DATE_FORMAT_1);
    }

    public static Date parse2(String dateStr) {
        return parse(dateStr, DATE_FORMAT_2);
    }

    public static Date parse3(String dateStr) {
        return parse(dateStr, DATE_FORMAT_3);
    }

    public static String format(Date date, DateFormat dateFormat) {
        return dateFormat.format(date);
    }

    public static String format1(Date date) {
        return format(date, DATE_FORMAT_1);
    }

    public static String format2(Date date) {
        return format(date, DATE_FORMAT_2);
    }

    public static String format3(Date date) {
        return format(date, DATE_FORMAT_3);
    }
}
