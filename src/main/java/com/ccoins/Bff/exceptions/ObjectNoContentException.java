package com.ccoins.Bff.exceptions;

import static java.text.MessageFormat.format;

public class ObjectNoContentException extends CustomException {

    public ObjectNoContentException(String code, Class<?> object) {
        super(code, format("{0}NoContent", object.getSimpleName()));
    }
}
