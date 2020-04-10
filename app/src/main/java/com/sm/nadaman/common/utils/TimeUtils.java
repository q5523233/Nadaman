package com.sm.nadaman.common.utils;

import android.util.Log;


import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by shenmai_vea on 2018/7/10.
 */

public class TimeUtils {
    private static Time time;

    private static void initTime() {
        time = new Time(System.currentTimeMillis());
    }

    public static String getCurrentTimeToString (String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
    public static Date getDateForString (String source,String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);// HH:mm:ss
        Date date = null;
        try {
            date = simpleDateFormat.parse(source);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }
}
