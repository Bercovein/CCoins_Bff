package com.ccoins.bff.utils;

import java.time.LocalDateTime;
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
}
