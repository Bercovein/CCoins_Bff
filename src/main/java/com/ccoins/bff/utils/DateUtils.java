package com.ccoins.bff.utils;

import java.time.Duration;
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

    public static String nowCurrentMillis(){
        return String.valueOf(System.currentTimeMillis());
    }


    public static Date nowDate(){
       return new Date(System.currentTimeMillis());
    }

    public static Date nowPlusDate(long time){
        return new Date(System.currentTimeMillis() + time);
    }

    public static boolean isBetweenLocalTimes(LocalTime time, LocalTime start, LocalTime end){
        return isAfterLocalTimes(time, start) && isBeforeLocalTimes(time, end);
    }

    public static boolean isNowBetweenLocalTimes(LocalTime start, LocalTime end){
        boolean response = true;
        if(start != null && end != null)
            response = DateUtils.isBetweenLocalTimes(LocalTime.now(), start, end);
        return response;
    }

    public static boolean isAfterLocalTimes(LocalTime time, LocalTime start){
        return time.isAfter(start);
    }

    public static boolean isBeforeLocalTimes(LocalTime time, LocalTime end){
        return time.isBefore(end);
    }

    public static boolean isAfterLocalDateTime(LocalDateTime time, LocalDateTime start){
        return time.isAfter(start);
    }

    public static boolean isBeforeLocalDateTime(LocalDateTime time, LocalDateTime start){
        return time.isBefore(start);
    }

    public static boolean isBetweenLocalDateTime(LocalDateTime time, LocalDateTime start, LocalDateTime end){
        return DateUtils.isAfterLocalDateTime(time,start) && DateUtils.isBeforeLocalDateTime(time, end);
    }

    public static boolean isLowerFromNowThan(LocalDateTime time, int seconds){
        Duration duration = Duration.between(time, LocalDateTime.now());
        return duration.getSeconds() < seconds;
    }

}
