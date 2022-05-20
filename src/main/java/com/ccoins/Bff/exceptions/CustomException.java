package com.ccoins.Bff.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@Data
@NoArgsConstructor
public abstract class CustomException extends RuntimeException implements Supplier {
    private String code;

    public CustomException(String code){
        super();
        this.code = code;
    }

    public CustomException(String code, String message){
        super(message);
        this.code = code;
    }
}
