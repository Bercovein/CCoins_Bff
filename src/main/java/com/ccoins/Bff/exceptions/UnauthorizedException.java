package com.ccoins.Bff.exceptions;

import static java.text.MessageFormat.format;

public class UnauthorizedException extends CustomException {

    public UnauthorizedException(String code, Class<?> object) {
        super(code, format("{0}Unauthorized", object.getSimpleName()));
    }

    public UnauthorizedException(String code, Class<?> object, String message) {
        super(code, format("{0}: {1}", object.getSimpleName(), message));
    }

    @Override
    public Object get() {
        return null;
    }
}
