package com.ccoins.bff.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class DateUtils {

    public static final String FORMAT_DDMMYYYY_HHMMSS_WITH_DASH= "DD-MM-YYYY HH:MM:SS";
    public static final String HH_MM = "HH:mm";

    public static final String DDMMYYYY_HHMM = "DD/MM/YYYY HH:mm";

    public static LocalDateTime nowLocalDateTime(){
        return LocalDateTime.now();
    }

    public static Date nowDate(){
       return new Date(System.currentTimeMillis());
    }

    public static Date nowPlusDate(long time){
        return new Date(System.currentTimeMillis() + time);
    }

    public static boolean isBetweenLocalTimes(LocalTime time, LocalTime start, LocalTime end){
        return time.isAfter(start) && time.isBefore(end);
    }

    public static boolean isNowBetweenLocalTimes(LocalTime start, LocalTime end){
        boolean response = true;
        if(start != null && end != null)
            response = DateUtils.isBetweenLocalTimes(LocalTime.now(), start, end);
        return response;
    }
}
