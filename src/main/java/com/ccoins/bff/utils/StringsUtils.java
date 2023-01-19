package com.ccoins.bff.utils;

import org.apache.logging.log4j.util.Strings;

public class StringsUtils {

    public static boolean isNullOrEmpty(String string){
        return string == null || Strings.isEmpty(string);
    }

}
