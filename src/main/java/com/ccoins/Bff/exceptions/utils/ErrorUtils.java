package com.ccoins.Bff.exceptions.utils;

import com.ccoins.Bff.exceptions.dto.ResponseDTO;

public class ErrorUtils {

    private static final String ERROR_LABEL = "[ERROR] ";

    public static ResponseDTO buildMessage(String code, Object message){

        return ResponseDTO.builder().code(code).data(null).message(message).build();
    }

    public static ResponseDTO buildMessage(String code, Object message, Object data){

        return ResponseDTO.builder().code(code).data(data).message(message).build();
    }

}
