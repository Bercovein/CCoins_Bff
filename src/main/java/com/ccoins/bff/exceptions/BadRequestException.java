package com.ccoins.bff.exceptions;

public class BadRequestException extends CustomException {

    private String message = "Invalid Parameters. - ";

    public BadRequestException(){
        super("BadRequest");
    }

    public BadRequestException(String code, Class<?> object, String message){
        super(code, message);
    }

    @Override
    public Object get() {
        return null;
    }
}
