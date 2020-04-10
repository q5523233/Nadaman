package com.sm.nadaman.common.utils;

import com.haibin.calendarview.Calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shenmai_vea on 2018/6/14.
 */

public class CalendarUtils {
    final static String TIME_FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";
    /**
     * 获取日历上标记的元素
     * @param list
     * @return
     */
    public static Map<String,Calendar> getSchemeDateListFromList(List<String> list) {
        int oldDay = 0
                ,oldMonth = 0
                ,oldYear = 0;
        Map<String,Calendar> calender = new HashMap<String,Calendar>();
        for (String t : list){
            Date date = TimeUtils.getDateForString(t, TIME_FORMAT_YMDHMS);
            java.util.Calendar instance = java.util.Calendar.getInstance();
            instance.setTime(date);

            int year  = instance.get(instance.YEAR);
            int month  = (instance.get(instance.MONTH)+1);
            int day  = instance.get(instance.DAY_OF_MONTH);

            if (day == oldDay){
                if (month == oldMonth)
                    if (year == oldYear )
                        continue;
            }else {
                oldDay = day;
                oldMonth = month;
                oldYear = year;
            }
            calender.put(getSchemeCalendar(year,month,day,0xFFdf1356).toString(),getSchemeCalendar(year,month,day,0xFFdf1356));
        }
        return calender;

    }


    private static Calendar getSchemeCalendar(int year, int month, int day, int color) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        return calendar;
    }
}
