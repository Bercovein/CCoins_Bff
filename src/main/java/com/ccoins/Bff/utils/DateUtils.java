package com.ccoins.Bff.utils;

import java.time.LocalDateTime;
import java.util.Date;

public class DateUtils {

    public static final String FORMAT_DDMMYYYY_HHMMSS_WITH_DASH= "DD-MM-YYYY HH:MM:SS";

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
