package com.ccoins.Bff.exceptions;

public class ForbiddenException extends CustomException {

    public ForbiddenException(String code, String message){
        super(code,message);
    }
}
