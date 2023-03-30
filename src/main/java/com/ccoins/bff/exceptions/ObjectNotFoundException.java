package com.ccoins.bff.exceptions;

public class ObjectNotFoundException extends CustomException {

    public ObjectNotFoundException(String code, Class<?> object, String message) {
        super(code, message);
    }

    @Override
    public Object get() {
        return null;
    }
}
