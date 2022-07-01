package com.ccoins.bff.exceptions;

import static java.text.MessageFormat.format;

public class BadRequestException extends CustomException {

    private String message = "Invalid Parameters. - ";

    public BadRequestException(){
        super("BadRequest");
    }

    public BadRequestException(String code, Class<?> object, String message){
        super(code,format("{0}BadRequest: {1}", object.getSimpleName(), message));
    }

    @Override
    public Object get() {
        return null;
    }
}
