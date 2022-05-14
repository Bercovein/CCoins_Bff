package com.ccoins.Bff.exceptions;

import static java.text.MessageFormat.format;

public class BadRequestException extends CustomException {

    private String message = "Invalid Parameters. - ";

    public BadRequestException(){
        super("BadRequest");
    }

    public BadRequestException(String code, Class<?> object){
        super(code,format("{0}BadRequest", object.getSimpleName()));
    }

}
