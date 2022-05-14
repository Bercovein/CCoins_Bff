package com.ccoins.Bff.exceptions;

import static java.text.MessageFormat.format;

public class ObjectNotFoundException extends CustomException {

    public ObjectNotFoundException(String code, Class<?> object) {
        super(code, format("{0}NotFound", object.getSimpleName()));
    }

}
