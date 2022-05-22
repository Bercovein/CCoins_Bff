package com.ccoins.Bff.utils;

import java.time.LocalDateTime;
import java.util.Date;

public class DateUtils {

    public static LocalDateTime nowLocalDateTime(){
        return LocalDateTime.now();
    }

    public static Date nowDate(){
       return new Date(System.currentTimeMillis());
    }

    public static Date nowPlusDate(int time){
        return new Date(System.currentTimeMillis() + time);
    }
}
