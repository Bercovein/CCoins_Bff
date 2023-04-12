package com.ccoins.bff.utils;

import org.apache.logging.log4j.util.Strings;

import java.util.Base64;

public class StringsUtils {

    public static boolean isNullOrEmpty(String string){
        return string == null || Strings.isEmpty(string);
    }

    public static String stringToBase64(String str){
        return Base64.getEncoder().encodeToString(str.getBytes());
    }
}
