package com.ccoins.bff.exceptions;

public class ForbiddenException extends CustomException {

    public ForbiddenException(String code, String message){
        super(code,message);
    }

    @Override
    public Object get() {
        return null;
    }
}
