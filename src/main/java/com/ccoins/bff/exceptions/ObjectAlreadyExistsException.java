package com.ccoins.bff.exceptions;

import static java.text.MessageFormat.format;

public class ObjectAlreadyExistsException extends CustomException {

    public ObjectAlreadyExistsException(String code, Class<?> object){
        super(code,format("{0}AlreadyExist", object.getSimpleName()));
    }

    @Override
    public Object get() {
        return null;
    }
}
