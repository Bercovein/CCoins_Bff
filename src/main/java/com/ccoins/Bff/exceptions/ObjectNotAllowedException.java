package com.ccoins.Bff.exceptions;

import static java.text.MessageFormat.format;

public class ObjectNotAllowedException extends CustomException {

    public ObjectNotAllowedException(String code, Class<?> object) {
        super(code, format("{0}NotAllowed", object.getSimpleName()));
    }


    @Override
    public Object get() {
        return null;
    }
}
