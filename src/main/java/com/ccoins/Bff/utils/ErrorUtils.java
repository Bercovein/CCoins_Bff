package com.ccoins.Bff.utils;

public class ErrorUtils {

    private static final String ERROR_LABEL = "[ERROR] ";

    public static String parseMethodError(Class<?> className) {
        try {
            return ERROR_LABEL.concat(className.getSimpleName()).concat(className.getEnclosingClass().getSimpleName());
        }catch(Exception e){
            throw new RuntimeException();
        }
    }
}
