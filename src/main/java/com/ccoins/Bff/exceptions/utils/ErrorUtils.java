package com.ccoins.Bff.exceptions.utils;

import com.ccoins.Bff.exceptions.dto.ExceptionRsDTO;

public class ErrorUtils {

    private static final String ERROR_LABEL = "[ERROR] ";

    public static ExceptionRsDTO buildMessage(String code, Object message){

        return ExceptionRsDTO.builder().code(code).message(message).build();
    }

    public static String parseMethodError(Class<?> className) {
        try {
            return ERROR_LABEL.concat(className.getSimpleName()).concat(className.getSimpleName());
        }catch(Exception e){
            throw new RuntimeException();
        }
    }
}
