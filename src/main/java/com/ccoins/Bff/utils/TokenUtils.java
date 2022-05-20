package com.ccoins.Bff.utils;

import com.ccoins.Bff.exceptions.ObjectNotFoundException;
import com.ccoins.Bff.exceptions.constant.ExceptionConstant;
import org.springframework.http.HttpHeaders;

public class TokenUtils {

    public static final String TOKEN_EMAIL = "sub";

    public static String get(HttpHeaders headers, String variable){

        if (headers == null) {
            throw new ObjectNotFoundException(
                    ExceptionConstant.TOKEN_VARIABLE_NOT_FOUND_ERROR_CODE,
                    TokenUtils.class,
                    ExceptionConstant.TOKEN_VARIABLE_NOT_FOUND_ERROR
            );
        }
        return headers.getFirst(variable);
    }
}

